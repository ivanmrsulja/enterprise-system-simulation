package util

import (
	"fmt"
	"crypto/sha256"
)

func Bytes2StrRaw(b [32]byte) string {
    return fmt.Sprintf("%x", b)
}

func Hash(str string) [32]byte {
	return sha256.Sum256([]byte(str))
}
