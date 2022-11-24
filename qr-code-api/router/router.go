package router

import (
	"github.com/gorilla/mux"
	"log"
	"net/http"
	"qr-code-api/handlers"
)

func HandleRequests() {
	router := mux.NewRouter()

	router.HandleFunc("/api/qr-code-generate", handlers.GenQrCode).Methods(http.MethodPost)
	router.HandleFunc("/api/qr-code-generate", handlers.ValidateData).Methods(http.MethodPost)

	log.Fatal(http.ListenAndServeTLS(":8085", "ssl/selfsigned.cer", "ssl/selfsigned.key", router))
}
