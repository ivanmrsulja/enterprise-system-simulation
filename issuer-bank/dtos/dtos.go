package dtos

import (
	_ "gopkg.in/validator.v2"
)

type TransactionState string

const (
    SUCCESS TransactionState = "SUCCESS"
    FAILED					 = "FAILED"
    ERROR					 = "ERROR"
)

func (s TransactionState) String() string {
	switch s {
	case SUCCESS:
		return "SUCCESS"
	case FAILED:
		return "FAILED"
	case ERROR:
		return "ERROR"
	}
	return "unknown" // ITLEKYAG <3
}

type IssuerBankRequest struct {
	Pan string `json:"pan" validate:"regexp=[0-9]{10}"`
	SecurityCode string `json:"securityCode" validate:"regexp=[0-9]{3}"`
	CardHolderName string `json:"cardHolderName"`
	ExpiryDate string `json:"expiryDate" validate:"regexp=[0-9]{2}/[0-9]{2}"`
	MerchantOrderId int `json:"merchantOrderId" validate:"min=1000000000,max=9999999999"`
	PaymentId int `json:"paymentId" validate:"min=1000000000,max=9999999999"`
	AcquirerOrderId int `json:"acquirerOrderId" validate:"min=1000000000,max=9999999999"`
	AcquirerTimestamp string `json:"acquirerTimestamp"`
	Amount float64 `json:"amount" validate:"min=0"`
}

type IssuerBankResponse struct {
	MerchantOrderId int `json:"merchantOrderId"`
	PaymentId int `json:"paymentId"`
	AcquirerOrderId int `json:"acquirerOrderId"`
	AcquirerTimestamp string `json:"acquirerTimestamp"`
	IssuerOrderId int `json:"issuerOrderId"`
	IssuerTimestamp string `json:"issuerTimestamp"`
	TransactionState TransactionState `json:"transactionState"`
}

type ErrorResponse struct {
	Message    string `json:"message"`
	StatusCode int    `json:"statusCode"`
}