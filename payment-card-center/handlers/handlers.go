package handlers

import (
	"bytes"
	"encoding/json"
	"io"
	"log"
	"net/http"

	dto "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/dtos"
	model "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/model"
	repository "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/repository"
	util "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/util"
	"gopkg.in/validator.v2"
)

func IssuerRequestHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	if !util.Authenticated(w, r) {
		return
	}

	// Validate request
	var bankRequest dto.IssuerBankRequest
	json.NewDecoder(r.Body).Decode(&bankRequest)
	if errs := validator.Validate(bankRequest); errs != nil {
		w.WriteHeader(http.StatusNotFound)
		json.NewEncoder(w).Encode(dto.ErrorResponse{Message: errs.Error(), StatusCode: http.StatusNotFound})
		return
	}

	// Log request
	fields := util.ExtractRequestFieldsForLogging(&bankRequest)
	util.LogHttpRequest(r, "IssuerRequestHandler - IssuerBankRequest", &fields)

	// Redirect request to issuer bank
	var buf bytes.Buffer
	err := json.NewEncoder(&buf).Encode(bankRequest)
	if err != nil {
		log.Println(err)
	}

	bank, _ := repository.FindIssuerBank(bankRequest.Pan) // Handle error
	host := chooseIssuerBank(bank)
	req, _ := http.NewRequest(http.MethodPatch, host, &buf)
	req.Header.Set("Accept", "application/json")
	req.Header.Set("X-Auth-Token", util.ApiKey)
	client := &http.Client{}
	response, err := client.Do(req)
	if err != nil {
		log.Println(err)
		w.WriteHeader(http.StatusGatewayTimeout)
		return
	}

	// Log response
	var bankResponse dto.IssuerBankResponse
	json.NewDecoder(response.Body).Decode(&bankResponse)
	fields = util.ExtractResponseFieldsForLogging(&bankResponse)
	util.LogHttpRequest(r, "IssuerRequestHandler - IssuerBankResponse", &fields)

	// Delegate response
	restoreResponseBody(response, &bankResponse)
	util.DelegateResponse(response, w)
}

func chooseIssuerBank(bank model.BankName) string {
	host := ""
	switch bank {
	case model.BANK1:
		host = util.BaseIssuerBank1PathRoundRobin.Next().Host
	case model.BANK2:
		host = util.BaseIssuerBank2PathRoundRobin.Next().Host
	}
	return host
}

func restoreResponseBody(response *http.Response, bankResponse *dto.IssuerBankResponse) {
	var responseBuffer bytes.Buffer
	err := json.NewEncoder(&responseBuffer).Encode(*bankResponse)
	if err != nil {
		log.Println(err)
	}

	response.Body = io.NopCloser(bytes.NewReader(responseBuffer.Bytes()))
}
