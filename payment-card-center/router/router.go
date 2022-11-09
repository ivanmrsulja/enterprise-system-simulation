package router

import (
	"log"
	"net/http"

	"github.com/gorilla/mux"
	handler "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/handlers"
)

func HandleRequests() {
	router := mux.NewRouter()
	router.HandleFunc("/api/pcc-redirect", handler.IssuerRequestHandler).Methods("PATCH")

	log.Fatal(http.ListenAndServeTLS(":8083", "ssl/selfsigned.cer", "ssl/selfsigned.key", router))
}
