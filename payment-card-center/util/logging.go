package util

import (
	"net"
	"net/http"
	"os"
	"os/signal"
	"strconv"

	log "log"

	dto "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/dtos"
	logger "github.com/sirupsen/logrus"
)

func ConfigureLogging() {
	logfileSystem, err := os.OpenFile("logs/system_logs.txt", os.O_WRONLY|os.O_CREATE|os.O_APPEND, 0644)
	if err != nil {
		log.Fatal(err)
	}
	log.SetOutput(logfileSystem)

	logfileReqests, err := os.OpenFile("logs/request_logs.txt", os.O_WRONLY|os.O_CREATE|os.O_APPEND, 0644)
	if err != nil {
		log.Fatal(err)
	}

	logger.SetOutput(logfileReqests)
	logger.SetFormatter(&logger.JSONFormatter{})

	configureGracefulShutdown()
}

func configureGracefulShutdown() {
	sigint := make(chan os.Signal, 1)
	signal.Notify(sigint, os.Interrupt)
	go func() {
		for sig := range sigint {
			// sig is a ^C
			log.Println("Signal -", sig, ". Shutting down gracefully...")
			os.Exit(1)
		}
	}()
}

func LogHttpRequest(request *http.Request, handlerName string, fields *map[string]string) {
	ip, port, err := net.SplitHostPort(request.RemoteAddr)

	if err != nil {
		logger.Error("Error parsing IP - DummyHandler")
	}

	userIP := net.ParseIP(ip)
	base := logger.WithFields(
		logger.Fields{
			"address": userIP,
			"port":    port,
		},
	)

	for key, value := range *fields {
		base = base.WithFields(
			logger.Fields{
				key: value,
			},
		)
	}

	base.Info("HTTP request - ", handlerName)
}

func ExtractRequestFieldsForLogging(bankResponse *dto.IssuerBankRequest) map[string]string {
	fields := make(map[string]string)
	fields["MerchantOrderId"] = strconv.Itoa(bankResponse.MerchantOrderId)
	fields["PaymentId"] = strconv.Itoa(bankResponse.PaymentId)
	fields["AcquirerOrderId"] = strconv.Itoa(bankResponse.AcquirerOrderId)
	fields["AcquirerTimestamp"] = bankResponse.AcquirerTimestamp

	return fields
}

func ExtractResponseFieldsForLogging(bankResponse *dto.IssuerBankResponse) map[string]string {
	fields := make(map[string]string)
	fields["MerchantOrderId"] = strconv.Itoa(bankResponse.MerchantOrderId)
	fields["PaymentId"] = strconv.Itoa(bankResponse.PaymentId)
	fields["AcquirerOrderId"] = strconv.Itoa(bankResponse.AcquirerOrderId)
	fields["AcquirerTimestamp"] = bankResponse.AcquirerTimestamp
	fields["IssuerOrderId"] = strconv.Itoa(bankResponse.IssuerOrderId)
	fields["IssuerTimestamp"] = bankResponse.IssuerTimestamp
	fields["TransactionState"] = bankResponse.TransactionState.String()

	return fields
}
