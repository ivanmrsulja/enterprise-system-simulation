package repository

import (
	"errors"
	"strconv"

	model "github.com/ivanmrsulja/enterprise-system-simulation/acquirer-bank/model"
	util "github.com/ivanmrsulja/enterprise-system-simulation/acquirer-bank/util"
)

func FindBankAccount(pan string, securityCode string, cardHolderName string, expiryDate string) (model.BankAccount, error) {
	var account model.BankAccount

	util.Db.Where(&model.BankAccount{
		Pan:            util.Bytes2StrRaw(util.Hash(pan)),
		SecurityCode:   util.Bytes2StrRaw(util.Hash(securityCode)),
		CardHolderName: util.Bytes2StrRaw(util.Hash(cardHolderName)),
		ExpiryDate:     util.Bytes2StrRaw(util.Hash(expiryDate))}).First(&account)

	if account.ID == 0 {
		return account, errors.New("bank account with given credentials does not exist")
	}

	return account, nil
}

func AuthenticateMerchantAccount(merchantId string, merchantPassword string) (model.MerchantAccount, error) {
	var account model.MerchantAccount

	util.Db.Where(&model.MerchantAccount{
		MerchantId:       merchantId,
		MerchantPassword: util.Bytes2StrRaw(util.Hash(merchantPassword)),
	}).First(&account)

	if account.ID == 0 {
		return account, errors.New("merchant account with given credentials does not exist")
	}

	return account, nil
}

func FindOrderTransaction(paymentId int) (model.Transaction, error) {
	var transaction model.Transaction

	util.Db.Where(&model.Transaction{PaymentId: paymentId}).First(&transaction)

	if transaction.ID == 0 {
		return transaction, errors.New("transaction with given payment ID does not exist")
	}

	return transaction, nil
}

func DeleteOrderTransaction(paymentId int) error {
	var transactionToDelete model.Transaction

	util.Db.Where(&model.Transaction{PaymentId: paymentId}).First(&transactionToDelete)

	if transactionToDelete.ID == 0 {
		return errors.New("transaction with given payment ID does not exist")
	}

	util.Db.Delete(transactionToDelete)

	return nil
}

func SaveTransaction(transaction model.Transaction) {
	util.Db.Save(&transaction)
}

func UpdateAccountBalance(newBalance float64, id uint) error {
	var accountToUpdate model.BankAccount

	util.Db.First(&accountToUpdate, id)

	if accountToUpdate.ID == 0 {
		return errors.New("account with ID " + strconv.FormatUint(uint64(id), 10) + " does not exist")
	}

	accountToUpdate.Balance = newBalance
	result := util.Db.Save(&accountToUpdate)

	return result.Error
}

func UpdateMerchantAccountBalance(newResources float64, merchantId string) error {
	var accountToUpdate model.MerchantAccount

	util.Db.Where(&model.MerchantAccount{
		MerchantId: merchantId,
	}).First(&accountToUpdate)

	if accountToUpdate.ID == 0 {
		return errors.New("merchant account with merchant ID " + merchantId + " does not exist")
	}

	accountToUpdate.Balance += newResources
	result := util.Db.Save(&accountToUpdate)

	return result.Error
}
