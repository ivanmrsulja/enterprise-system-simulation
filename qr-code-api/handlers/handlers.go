package handlers

import (
	"encoding/json"
	"fmt"
	"github.com/skip2/go-qrcode"
	"net/http"
	"qr-code-api/dtos"
	"qr-code-api/util"
)

func GenQrCode(w http.ResponseWriter, r *http.Request) {
	var dataToCode dtos.DataToCode
	w.Header().Set("Content-Type", "application/json")

	err := json.NewDecoder(r.Body).Decode(&dataToCode)

	//if errs := validator.Validate(bankRequest); errs != nil {
	//	w.WriteHeader(http.StatusBadRequest)
	//	json.NewEncoder(w).Encode(dto.ErrorResponse{Message: errs.Error(), StatusCode: http.StatusBadRequest})
	//	return
	//}

	if err != nil {
		// Vrati razuman response
		fmt.Println(err.Error())
		return
	}

	dataToCodeStr := util.ConvertJsonToString(dataToCode)
	encode, err := qrcode.Encode(dataToCodeStr, qrcode.Medium, 150)
	if err != nil {
		fmt.Println(err.Error())
		return
	}
	fmt.Println(encode)
}

// ValidateQrCode TODO Implement method for qr code validation
func ValidateQrCode(w http.ResponseWriter, r *http.Request) {}
