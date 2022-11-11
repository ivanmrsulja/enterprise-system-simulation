package util

import (
	"encoding/json"
	"io"
	"log"
	"net/http"
	"os"

	dto "github.com/ivanmrsulja/enterprise-system-simulation/issuer-bank/dtos"
)

var ApiKey string

func LoadApiKey() {
	file, err := os.Open("ssl/apiKey.txt")
	if err != nil {
		log.Fatal(err)
	}
	defer func() {
		if err = file.Close(); err != nil {
			log.Fatal(err)
		}
	}()
	b, err := io.ReadAll(file)
	ApiKey = string(b)
}

func Authenticated(w http.ResponseWriter, r *http.Request) bool {
	apiKey := r.Header.Get("X-Auth-Token")
	if apiKey != ApiKey {
		w.WriteHeader(http.StatusUnauthorized)
		json.NewEncoder(w).Encode(dto.ErrorResponse{Message: "Bad API key.", StatusCode: http.StatusUnauthorized})
		return false
	}
	return true
}
