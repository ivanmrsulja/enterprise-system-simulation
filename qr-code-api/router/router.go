package router

import (
	"github.com/gorilla/mux"
	"log"
	"net/http"
	"qr-code-api/handlers"
)

func HandleRequests() {
	router := mux.NewRouter()

	router.HandleFunc("/api/qr-code-generate", handlers.GenQrCode).Methods(http.MethodPost, http.MethodOptions)
	router.HandleFunc("/api/qr-code-validate", handlers.ValidateData).Methods(http.MethodPost, http.MethodOptions)

	log.Fatal(http.ListenAndServeTLS(":8085", "ssl/selfsigned.cer", "ssl/selfsigned.key", router))
}
