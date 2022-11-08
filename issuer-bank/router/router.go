package router

import (
	"log"
	"net/http"

	"github.com/gorilla/mux"
	handler "github.com/ivanmrsulja/enterprise-system-simulation/issuer-bank/handlers"
)

func HandleRequests() {
	router := mux.NewRouter()
	router.HandleFunc("/api/issuer-bank", handler.IssuerRequestHandler).Methods("PUT")

	log.Fatal(http.ListenAndServeTLS(":8082", "ssl/test.cer", "ssl/test.key", router))
}
