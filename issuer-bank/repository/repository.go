package repository

import (
	"errors"
	"strconv"

	model "github.com/ivanmrsulja/enterprise-system-simulation/issuer-bank/model"
	util "github.com/ivanmrsulja/enterprise-system-simulation/issuer-bank/util"
)


func FindBankAccount(pan string, securityCode string, cardHolderName string, expiryDate string) (model.BankAccount, error) {
	var account model.BankAccount

	util.Db.Where(&model.BankAccount{Pan: util.Bytes2StrRaw(util.Hash(pan)), 
									SecurityCode: util.Bytes2StrRaw(util.Hash(securityCode)), 
									CardHolderName: util.Bytes2StrRaw(util.Hash(cardHolderName)), 
									ExpiryDate: util.Bytes2StrRaw(util.Hash(expiryDate))}).First(&account)

	if account.ID == 0 {
		return account, errors.New("Bank account with given credentials does not exist.")
	}

	return account, nil
}

func UpdateAccountBalance(newBalance float64, id uint) error {
	var accountToUpdate model.BankAccount

	util.Db.First(&accountToUpdate, id)

	if accountToUpdate.ID == 0 {
		return errors.New("Account with ID " + strconv.FormatUint(uint64(id), 10) + " does not exist.")
	}

	accountToUpdate.Balance = newBalance
	result := util.Db.Save(&accountToUpdate)
	
	return result.Error
}
