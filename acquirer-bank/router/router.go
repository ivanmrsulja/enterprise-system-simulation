package router

import (
	"log"
	"net/http"

	"github.com/gorilla/mux"
	handler "github.com/ivanmrsulja/enterprise-system-simulation/acquirer-bank/handlers"
)

func HandleRequests() {
	router := mux.NewRouter()
	router.HandleFunc("/api/acquirer-bank/credit-card-payment", handler.CreditCardPaymentHandler).Methods("PATCH", "OPTIONS")
	router.HandleFunc("/api/acquirer-bank/qr-code-payment", handler.QrCodePaymentHandler).Methods("PATCH", "OPTIONS")
	router.HandleFunc("/api/acquirer-bank/request-redirect", handler.RedirectHandler).Methods("POST", "OPTIONS")
	router.HandleFunc("/api/acquirer-bank/authenticate", handler.AuthenticateMerchant).Methods("POST", "OPTIONS")
	router.HandleFunc("/api/acquirer-bank/transaction-details/{paymentId}", handler.GetTransactionDetails).Methods("GET", "OPTIONS")

	log.Fatal(http.ListenAndServeTLS(":8082", "ssl/selfsigned.cer", "ssl/selfsigned.key", router))
}
