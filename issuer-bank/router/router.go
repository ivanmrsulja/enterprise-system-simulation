package router

import (
	"log"
	"net/http"

	"github.com/gorilla/mux"
	handler "github.com/ivanmrsulja/enterprise-system-simulation/issuer-bank/handlers"
)

func HandleRequests() {
	router := mux.NewRouter()
	router.HandleFunc("/api/issuer-bank", handler.IssuerRequestHandler).Methods("PATCH")

	log.Fatal(http.ListenAndServeTLS(":8084", "ssl/selfsigned.cer", "ssl/selfsigned.key", router))
}
