package handlers

import (
	"bytes"
	"encoding/json"
	"io"
	"log"
	"math/rand"
	"net/http"
	"strconv"
	"time"

	"gopkg.in/validator.v2"

	"github.com/gorilla/mux"
	dto "github.com/ivanmrsulja/enterprise-system-simulation/acquirer-bank/dtos"
	model "github.com/ivanmrsulja/enterprise-system-simulation/acquirer-bank/model"
	repository "github.com/ivanmrsulja/enterprise-system-simulation/acquirer-bank/repository"
	util "github.com/ivanmrsulja/enterprise-system-simulation/acquirer-bank/util"
)

func CreditCardPaymentHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	util.SetupResponse(&w, r)
	if r.Method == "OPTIONS" {
		return
	}
	
	var creditCardInfo dto.CreditCardInfo
	json.NewDecoder(r.Body).Decode(&creditCardInfo)

	if errs := validator.Validate(creditCardInfo); errs != nil {
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(dto.ErrorResponse{Message: errs.Error(), StatusCode: http.StatusBadRequest})
		return
	}

	transactionDetails, err := repository.FindOrderTransaction(creditCardInfo.PaymentId)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(dto.ErrorResponse{Message: err.Error(), StatusCode: http.StatusBadRequest})
		return
	}
	amount := transactionDetails.Amount

	if creditCardInfo.Pan[0:6] == "111111" {
		handleTransactionInHouse(w, r, &creditCardInfo, float64(amount), transactionDetails.MerchantId)
	} else {
		handleTransactionOverPCC(w, r, &creditCardInfo, float64(amount), transactionDetails.MerchantId)
	}
}

func handleTransactionInHouse(w http.ResponseWriter, r *http.Request, creditCardInfo *dto.CreditCardInfo, amount float64, merchantId string) {
	bankAccount, err := repository.FindBankAccount(creditCardInfo.Pan, creditCardInfo.SecurityCode, creditCardInfo.CardHolderName, creditCardInfo.ExpiryDate)

	var state dto.TransactionState
	state = dto.ERROR
	if err == nil {
		if bankAccount.Balance >= amount {
			err := repository.UpdateAccountBalance(bankAccount.Balance-amount, bankAccount.ID)
			if err == nil {
				state = dto.SUCCESS
			}
		} else {
			state = dto.FAILED
		}
	}

	acquirerOrderId, acquirerTimestamp := generateIdAndTimestamp()

	finalStep := dto.AcquirerBankFinalStep{
		MerchantOrderId:   creditCardInfo.MerchantOrderId,
		PaymentId:         creditCardInfo.PaymentId,
		AcquirerOrderId:   acquirerOrderId,
		AcquirerTimestamp: acquirerTimestamp,
		TransactionState:  state}

	fields := util.ExtractResponseFieldsForLogging(&finalStep)
	util.LogHttpRequest(r, "Credit Card Payment - Response", &fields)

	handleFinalPaymentStep(w, &finalStep)

	if state == dto.SUCCESS {
		repository.UpdateMerchantAccountBalance(amount, merchantId)
	}
	repository.DeleteOrderTransaction(finalStep.PaymentId)
}

func handleTransactionOverPCC(w http.ResponseWriter, r *http.Request, creditCardInfo *dto.CreditCardInfo, amount float64, merchantId string) {
	acquirerOrderId, acquirerTimestamp := generateIdAndTimestamp()

	bankRequest := dto.IssuerBankRequest{
		Pan:               creditCardInfo.Pan,
		SecurityCode:      creditCardInfo.SecurityCode,
		CardHolderName:    creditCardInfo.CardHolderName,
		ExpiryDate:        creditCardInfo.ExpiryDate,
		MerchantOrderId:   creditCardInfo.MerchantOrderId,
		PaymentId:         creditCardInfo.PaymentId,
		AcquirerOrderId:   acquirerOrderId,
		AcquirerTimestamp: acquirerTimestamp,
		Amount:            amount}

	fields := util.ExtractPCCRequestFieldsForLogging(&bankRequest)
	util.LogHttpRequest(r, "Credit Card Payment - PCC Request", &fields)

	var buf bytes.Buffer
	err := json.NewEncoder(&buf).Encode(bankRequest)
	if err != nil {
		log.Println(err)
	}

	req, _ := http.NewRequest(http.MethodPatch, util.BasePCCRedirectPathRoundRobin.Next().Host, &buf)
	req.Header.Set("Accept", "application/json")
	req.Header.Set("X-Auth-Token", util.ApiKey)
	client := &http.Client{}
	response, err := client.Do(req)
	if err != nil {
		log.Println(err)
		w.WriteHeader(http.StatusGatewayTimeout)
		return
	}

	var issuerBankResponse dto.IssuerBankResponse
	json.NewDecoder(response.Body).Decode(&issuerBankResponse)

	if issuerBankResponse.PaymentId == 0 {
		util.DelegateResponse(response, w)
		return
	}

	finalStep := dto.AcquirerBankFinalStep{
		MerchantOrderId:   issuerBankResponse.MerchantOrderId,
		PaymentId:         issuerBankResponse.PaymentId,
		AcquirerOrderId:   issuerBankResponse.AcquirerOrderId,
		AcquirerTimestamp: issuerBankResponse.AcquirerTimestamp,
		TransactionState:  issuerBankResponse.TransactionState}

	fields = util.ExtractResponseFieldsForLogging(&finalStep)
	util.LogHttpRequest(r, "Credit Card Payment - PCC Response", &fields)

	handleFinalPaymentStep(w, &finalStep)

	if issuerBankResponse.TransactionState == dto.SUCCESS {
		repository.UpdateMerchantAccountBalance(amount, merchantId)
	}
	repository.DeleteOrderTransaction(finalStep.PaymentId)
}

func handleFinalPaymentStep(w http.ResponseWriter, finalStep *dto.AcquirerBankFinalStep) {
	var buf bytes.Buffer
	err := json.NewEncoder(&buf).Encode(finalStep)
	if err != nil {
		log.Println(err)
	}

	req, _ := http.NewRequest(http.MethodPost, util.BasePSPRedirectPathRoundRobin.Next().Host, &buf)
	req.Header.Set("Accept", "application/json")
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("X-Auth-Token", util.ApiKey)
	client := &http.Client{}
	response, err := client.Do(req)
	if err != nil {
		log.Println(err)
		w.WriteHeader(http.StatusGatewayTimeout)
		return
	}

	io.Copy(w, response.Body)
	response.Body.Close()
}

func QrCodePaymentHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	if !util.Authenticated(w, r) {
		return
	}
	// TODO: nastavi tosicu
}

func RedirectHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	util.SetupResponse(&w, r)
	if r.Method == "OPTIONS" {
		return
	}

	if !util.Authenticated(w, r) {
		return
	}

	var paymentRequest dto.AcquirerBankPaymentRequest
	json.NewDecoder(r.Body).Decode(&paymentRequest)

	if errs := validator.Validate(paymentRequest); errs != nil {
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(dto.ErrorResponse{Message: errs.Error(), StatusCode: http.StatusBadRequest})
		return
	}

	account, err := repository.AuthenticateMerchantAccount(paymentRequest.MerchantId, paymentRequest.MerchantPassword)
	if err != nil {
		w.WriteHeader(http.StatusUnauthorized)
		json.NewEncoder(w).Encode(dto.ErrorResponse{Message: err.Error(), StatusCode: http.StatusUnauthorized})
		return
	}

	paymentId, _ := generateIdAndTimestamp()

	transaction := model.Transaction{PaymentId: paymentId, MerchantId: account.MerchantId, Amount: paymentRequest.Amount}
	repository.SaveTransaction(transaction)

	json.NewEncoder(w).Encode(dto.BankRedirectResponse{PaymentUrl: "http://127.0.0.1:5174/payment", PaymentId: paymentId})
}

func AuthenticateMerchant(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	if !util.Authenticated(w, r) {
		return
	}

	var authenticationRequest dto.AcquirerBankMerchantAuthentication
	json.NewDecoder(r.Body).Decode(&authenticationRequest)

	if errs := validator.Validate(authenticationRequest); errs != nil {
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(dto.ErrorResponse{Message: errs.Error(), StatusCode: http.StatusBadRequest})
		return
	}

	_, err := repository.AuthenticateMerchantAccount(authenticationRequest.MerchantId, authenticationRequest.MerchantPassword)
	if err != nil {
		w.WriteHeader(http.StatusUnauthorized)
		json.NewEncoder(w).Encode(false)
		return
	}

	json.NewEncoder(w).Encode(true)
}

func GetTransactionDetails(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	util.SetupResponse(&w, r)
	if r.Method == "OPTIONS" {
		return
	}
	
	id, _ := strconv.Atoi(mux.Vars(r)["paymentId"])
    
	transactionInfo, err := repository.GetTransactionInfo(id)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		return
	}
	json.NewEncoder(w).Encode(transactionInfo)
}

func generateIdAndTimestamp() (int, string) {
	return rand.Intn(9999999999-1000000000) + 1000000000, time.Now().String()
}
