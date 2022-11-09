package main

import (
	"log"
	"math/rand"
	"time"
	
	router "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/router"
	util "github.com/ivanmrsulja/enterprise-system-simulation/payment-card-center/util"
)

func main() {
	rand.Seed(time.Now().UnixNano())

	util.ConfigureLogging()

	log.Println("Starting server...")

	util.ConnectToDatabase()
	router.HandleRequests()
	defer util.Db.Close()
}
