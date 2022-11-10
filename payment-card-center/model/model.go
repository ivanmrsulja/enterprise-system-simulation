package model

import (
	"github.com/jinzhu/gorm"
)

type BankName int32

const (
	BANK1 BankName = iota
	BANK2
)

type RedirectTable struct {
	gorm.Model

	PanBankID string
	Bank      BankName
}
