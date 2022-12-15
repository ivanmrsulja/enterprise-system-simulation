package handlers

import (
	"encoding/json"
	"github.com/skip2/go-qrcode"
	"gopkg.in/validator.v2"
	"net/http"
	"qr-code-api/dtos"
	"qr-code-api/util"
)

func GenQrCode(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	util.SetupResponse(&w, r)
	if r.Method == "OPTIONS" {
		return
	}

	var dataToCode dtos.DataToCode

	err := json.NewDecoder(r.Body).Decode(&dataToCode)
	dataToCodeStr := util.ConvertJsonToString(dataToCode)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(dtos.CreateErrorResponse(dataToCodeStr, 620, "Invalid json in request", []string{"Invalid json in request"}))
		return
	}

	if errs := validator.Validate(dataToCode); errs != nil {
		w.WriteHeader(http.StatusBadRequest)
		errorsStr := dtos.ReplaceGenericMessage(errs.Error())
		errorsArr := dtos.CreateArrayOfString(errorsStr)
		json.NewEncoder(w).Encode(dtos.CreateErrorResponse(dataToCodeStr, 620, errorsStr, errorsArr))
		return
	}

	encoded, err := qrcode.Encode(dataToCodeStr, qrcode.Medium, 150)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		json.NewEncoder(w).Encode(dtos.CreateErrorResponse(dataToCodeStr, 500, "Unknown error occurred while generating qr code", []string{"Unknown error occurred while generating qr code"}))
		return
	}

	ret := dtos.CreateDataCoded(dataToCodeStr, dataToCode, encoded)
	json.NewEncoder(w).Encode(ret)
}

func ValidateData(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	util.SetupResponse(&w, r)
	if r.Method == "OPTIONS" {
		return
	}

	var dataToCode dtos.DataToCode

	err := json.NewDecoder(r.Body).Decode(&dataToCode)
	dataToCodeStr := util.ConvertJsonToString(dataToCode)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(w).Encode(dtos.CreateErrorResponse(dataToCodeStr, 620, "Invalid json in request", []string{"Invalid json in request"}))
		return
	}

	if errs := validator.Validate(dataToCode); errs != nil {
		w.WriteHeader(http.StatusBadRequest)
		errorsStr := dtos.ReplaceGenericMessage(errs.Error())
		errorsArr := dtos.CreateArrayOfString(errorsStr)
		json.NewEncoder(w).Encode(dtos.CreateErrorResponse(dataToCodeStr, 620, errorsStr, errorsArr))
		return
	}

	ret := dtos.CreateDataValidation(dataToCodeStr, dataToCode)
	json.NewEncoder(w).Encode(ret)
}
