package handlers

import (
	"encoding/json"
	"net/http"
	"bytes"
	"log"

	"gopkg.in/validator.v2"
	dto "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/dtos"
	util "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/util"
	model "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/model"
	repository "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/repository"
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

	fields := util.ExtractRequestFieldsForLogging(&bankRequest)
	util.LogHttpRequest(r, "IssuerRequestHandler - IssuerBankResponse", &fields)

	var buf bytes.Buffer
    err := json.NewEncoder(&buf).Encode(bankRequest)
    if err != nil {
        log.Println(err)
    }

	bank, _ := repository.FindIssuerBank(bankRequest.Pan) // Handle error

	host := ""
	switch bank {
	case model.BANK1:
		host = util.BaseIssuerBank1PathRoundRobin.Next().Host
	case model.BANK2:
		host = util.BaseIssuerBank2PathRoundRobin.Next().Host
	}

	req, _ := http.NewRequest(http.MethodPatch, host, &buf)
	req.Header.Set("Accept", "application/json")
	client := &http.Client{}
	response, err := client.Do(req)

	if err != nil {
		log.Println(err)
		w.WriteHeader(http.StatusGatewayTimeout)
		return
	}
	
	// fields = util.ExtractResponseFieldsForLogging(&bankResponse)
	// util.LogHttpRequest(r, "IssuerRequestHandler - IssuerBankResponse", &fields)

	util.DelegateResponse(response, w)
}
