package repository

import (
	"errors"

	model "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/model"
	util "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/util"
)

func FindIssuerBank(pan string) (model.BankName, error) {
	var redirect model.RedirectTable

	util.Db.Where(&model.RedirectTable{PanBankID: pan[0:6]}).First(&redirect)

	if redirect.ID == 0 {
		return redirect.Bank, errors.New("given PAN does not correspond with any known bank")
	}

	return redirect.Bank, nil
}
