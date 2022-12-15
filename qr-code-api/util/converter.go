package util

import (
	"fmt"
	"qr-code-api/dtos"
)

func ConvertJsonToString(d dtos.DataToCode) string {
	return fmt.Sprintf("K:%s|V:%s|C:%s|R:%s|N:%s|I:%s|SF:%s|S:%s", d.K, d.V, d.C, d.R, d.N, d.I, d.SF, d.S)
}
