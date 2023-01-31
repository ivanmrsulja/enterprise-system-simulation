package util

import (
	"log"

	model "github.com/ivanmrsulja/enterprise-system-simulation/issuer-bank/model"
	"github.com/jinzhu/gorm"
	_ "github.com/jinzhu/gorm/dialects/postgres"
)

var (
	accounts = []model.BankAccount{
		{Pan: Bytes2StrRaw(Hash("1231231231")), SecurityCode: Bytes2StrRaw(Hash("123")), CardHolderName: Bytes2StrRaw(Hash("Ivan Mrsulja")), ExpiryDate: Bytes2StrRaw(Hash("11/24")), Balance: 10000},
		{Pan: Bytes2StrRaw(Hash("1231213213")), SecurityCode: Bytes2StrRaw(Hash("321")), CardHolderName: Bytes2StrRaw(Hash("Milos Popovic")), ExpiryDate: Bytes2StrRaw(Hash("05/23")), Balance: 20000},
		{Pan: Bytes2StrRaw(Hash("1231211111")), SecurityCode: Bytes2StrRaw(Hash("456")), CardHolderName: Bytes2StrRaw(Hash("Veljko Tosic")), ExpiryDate: Bytes2StrRaw(Hash("08/25")), Balance: 300},
	}
)

var Db *gorm.DB
var err error

func ConnectToDatabase() {
	connectionString := "host=localhost user=postgres dbname=IssuerBankDB sslmode=disable password=root port=5432"
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
