package model

import (
	"github.com/jinzhu/gorm"
)

type BankAccount struct {
	gorm.Model

	Pan            string
	SecurityCode   string
	CardHolderName string
	ExpiryDate     string
	Balance        float64
}

type MerchantAccount struct {
	gorm.Model

	MerchantId       string
	MerchantPassword string
	Balance          float64
	MerchantName     string
}

type Transaction struct {
	gorm.Model
	PaymentId  int `sql:"type:bigint"`
	MerchantId string
	Amount     float64
}
