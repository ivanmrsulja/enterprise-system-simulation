package handlers

import (
	"encoding/json"
	"net/http"
	"time"
	"math/rand"

	"gopkg.in/validator.v2"
	dto "github.com/ivanmrsulja/enterprise-system-simulation/issuer-bank/dtos"
	util "github.com/ivanmrsulja/enterprise-system-simulation/issuer-bank/util"
	repository "github.com/ivanmrsulja/enterprise-system-simulation/issuer-bank/repository"
)

func IssuerRequestHandler(w http.ResponseWriter, r *http.Request) {
	var bankRequest dto.IssuerBankRequest
	json.NewDecoder(r.Body).Decode(&bankRequest)

	w.Header().Set("Content-Type", "application/json")
	if errs := validator.Validate(bankRequest); errs != nil {
		w.WriteHeader(http.StatusNotFound)
		json.NewEncoder(w).Encode(dto.ErrorResponse{Message: errs.Error(), StatusCode: http.StatusNotFound})
		return
	}

	bankAccount, err := repository.FindBankAccount(bankRequest.Pan, bankRequest.SecurityCode, bankRequest.CardHolderName, bankRequest.ExpiryDate)

	var state dto.TransactionState
	state = dto.ERROR
	if err == nil {
		if bankAccount.Balance >= bankRequest.Amount {
			err := repository.UpdateAccountBalance(bankAccount.Balance - bankRequest.Amount, bankAccount.ID)
			if err == nil {
				state = dto.SUCCESS
			}
		} else {
			state = dto.FAILED
		}	
	}
	
	bankResponse := dto.IssuerBankResponse{MerchantOrderId: bankRequest.MerchantOrderId, 
											PaymentId: bankRequest.PaymentId, 
								   			AcquirerOrderId: bankRequest.AcquirerOrderId,
								   			AcquirerTimestamp: bankRequest.AcquirerTimestamp,
								   			IssuerOrderId: rand.Intn(9999999999 - 1000000000) + 1000000000,
								   			IssuerTimestamp: time.Now().String(),
								   			TransactionState: state}
	
	fields := util.ExtractFieldsForLogging(&bankResponse)
	util.LogHttpRequest(r, "IssuerRequestHandler", &fields)

	json.NewEncoder(w).Encode(bankResponse)
}
