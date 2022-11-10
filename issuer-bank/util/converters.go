package util

import (
	"crypto/sha256"
	"fmt"
)

func Bytes2StrRaw(b [32]byte) string {
	return fmt.Sprintf("%x", b)
}

func Hash(str string) [32]byte {
	return sha256.Sum256([]byte(str))
}
