package util

import (
	"encoding/json"
	"io"
	"log"
	"net/http"
	"net/url"
	"os"

	roundrobin "github.com/hlts2/round-robin"
	dto "github.com/ivanmrsulja/enterprise-system-simulation/acquirer-bank/dtos"
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

var BasePCCRedirectPathRoundRobin, _ = roundrobin.New(
	&url.URL{Host: "https://localhost:8083/api/pcc-redirect"},
)

func DelegateResponse(response *http.Response, w http.ResponseWriter) {
	w.Header().Set("Content-Type", response.Header.Get("Content-Type"))
	w.Header().Set("Content-Length", response.Header.Get("Content-Length"))
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.WriteHeader(response.StatusCode)
	io.Copy(w, response.Body)
	response.Body.Close()
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