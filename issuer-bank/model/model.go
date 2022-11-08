package model

import (
	"github.com/jinzhu/gorm"
)

type BankAccount struct {
	gorm.Model

	Pan string
	SecurityCode string
	CardHolderName string
	ExpiryDate string
	Balance float64
}
