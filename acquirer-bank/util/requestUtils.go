package util

import (
	"io"
	"log"
	"os"
)

var ApiKey string

func LoadApiKey() {
	file, err := os.Open("ssl/apiKey.txt")
	if err != nil {
		log.Fatal(err)
	}
	defer func() {
		if err = file.Close(); err != nil {
			log.Fatal(err)
		}
	}()
	b, err := io.ReadAll(file)
	ApiKey = string(b)
}
