package main

import (
	"log"
	"math/rand"
	"time"

	router "github.com/ivanmrsulja/enterprise-system-simulation/acquirer-bank/router"
	util "github.com/ivanmrsulja/enterprise-system-simulation/acquirer-bank/util"
)

func main() {
	rand.Seed(time.Now().UnixNano())

	util.ConfigureLogging()

	log.Println("Starting server...")

	util.ConnectToDatabase()
	util.LoadApiKey()
	router.HandleRequests()
	defer util.Db.Close()
}
