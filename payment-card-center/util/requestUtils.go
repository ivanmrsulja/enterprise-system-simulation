package util

import (
	"io"
	"log"
	"net/http"
	"net/url"
	"os"

	roundrobin "github.com/hlts2/round-robin"
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

var BaseIssuerBank1PathRoundRobin, _ = roundrobin.New(
	&url.URL{Host: "https://localhost:8084/api/issuer-bank"},
)

var BaseIssuerBank2PathRoundRobin, _ = roundrobin.New(
	&url.URL{Host: "https://localhost:8085/api/issuer-bank"}, // Hipoteticka banka, mozda da roknemo issuer-bank u 2 kontejnera pa namapiramo na 2 port-a ? :)
)

func DelegateResponse(response *http.Response, w http.ResponseWriter) {
	w.Header().Set("Content-Type", response.Header.Get("Content-Type"))
	w.Header().Set("Content-Length", response.Header.Get("Content-Length"))
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.WriteHeader(response.StatusCode)
	io.Copy(w, response.Body)
	response.Body.Close()
}
