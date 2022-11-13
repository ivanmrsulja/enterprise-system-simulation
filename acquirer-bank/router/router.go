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
	router.HandleFunc("/api/acquirer-bank/request-redirect", handler.RedirectHandler).Methods("POST")
	router.HandleFunc("/api/acquirer-bank/authenticate", handler.AuthenticateMerchant).Methods("POST")

	log.Fatal(http.ListenAndServeTLS(":8082", "ssl/selfsigned.cer", "ssl/selfsigned.key", router))
}
