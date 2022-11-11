package router

import (
	"log"
	"net/http"

	"github.com/gorilla/mux"
	handler "github.com/ivanmrsulja/enterprise-system-simulation/acquirer-bank/handlers"
)

func HandleRequests() {
	router := mux.NewRouter()
	router.HandleFunc("/api/acquirer-bank/credit-card-payment", handler.CreditCardPaymentHandler).Methods("PATCH")
	router.HandleFunc("/api/acquirer-bank/qr-code-payment", handler.QrCodePaymentHandler).Methods("PATCH")
	router.HandleFunc("/api/acquirer-bank/register", handler.RegisterHandler).Methods("POST")

	log.Fatal(http.ListenAndServeTLS(":8084", "ssl/selfsigned.cer", "ssl/selfsigned.key", router))
}
