package util

import (
	"log"

	model "github.com/ivanmrsulja/enterprise-system-simulation/acquirer-bank/model"
	"github.com/jinzhu/gorm"
	_ "github.com/jinzhu/gorm/dialects/postgres"
)

var (
	accounts = []model.BankAccount{
		{Pan: Bytes2StrRaw(Hash("1111131231")), SecurityCode: Bytes2StrRaw(Hash("456")), CardHolderName: Bytes2StrRaw(Hash("Jovan Jovanovic")), ExpiryDate: Bytes2StrRaw(Hash("05/23")), Balance: 10000},
		{Pan: Bytes2StrRaw(Hash("1111113213")), SecurityCode: Bytes2StrRaw(Hash("654")), CardHolderName: Bytes2StrRaw(Hash("Milovan Jovic")), ExpiryDate: Bytes2StrRaw(Hash("07/23")), Balance: 20000},
		{Pan: Bytes2StrRaw(Hash("1111111111")), SecurityCode: Bytes2StrRaw(Hash("725")), CardHolderName: Bytes2StrRaw(Hash("Vesna Savic")), ExpiryDate: Bytes2StrRaw(Hash("10/24")), Balance: 30000},
	}
)

var Db *gorm.DB
var err error

func ConnectToDatabase() {
	connectionString := "host=localhost user=postgres dbname=AcquirerBankDB sslmode=disable password=root port=5432"
	dialect := "postgres"

	Db, err = gorm.Open(dialect, connectionString)
	if err != nil {
		log.Fatal(err)
	} else {
		log.Println("Connection to DB successfull.")
	}

	Db.DropTable("bank_accounts")
	Db.AutoMigrate(&model.BankAccount{})

	for _, account := range accounts {
		Db.Create(&account)
	}
}
