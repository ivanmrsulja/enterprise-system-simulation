package util

import (
	"log"

	model "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/model"
	"github.com/jinzhu/gorm"
	_ "github.com/jinzhu/gorm/dialects/postgres"
)

var (
	accounts = []model.RedirectTable{
		{PanBankID: "12312", Bank: model.BANK1},
		{PanBankID: "11111", Bank: model.BANK2},
	}
)

var Db *gorm.DB
var err error

func ConnectToDatabase() {
	connectionString := "host=localhost user=postgres dbname=PaymentCardCenterDB sslmode=disable password=root port=5432"
	dialect := "postgres"

	Db, err = gorm.Open(dialect, connectionString)
	if err != nil {
		log.Fatal(err)
	} else {
		log.Println("Connection to DB successfull.")
	}

	Db.DropTable("redirect_tables")
	Db.AutoMigrate(&model.RedirectTable{})

	for _, account := range accounts {
		Db.Create(&account)
	}
}
