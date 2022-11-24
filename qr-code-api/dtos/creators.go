package dtos

import (
	"strings"
)

func CreateDataCoded(dataToCodeStr string, dataToCode DataToCode, encoded []byte) DataCoded {
	return DataCoded{
		S: CodeDesc{
			Code: 0,
			Desc: "OK",
		},
		T: dataToCodeStr,
		N: dataToCode,
		I: encoded,
	}
}

func CreateDataValidation(dataToCodeStr string, dataToCode DataToCode) DataValidation {
	return DataValidation{
		S: CodeDesc{
			Code: 0,
			Desc: "OK",
		},
		T: dataToCodeStr,
		N: dataToCode,
	}
}

func CreateErrorResponse(dataToCodeStr string, code uint32, desc string, errors []string) ErrorResponse {
	return ErrorResponse{
		S: CodeDesc{
			Code: code,
			Desc: desc,
		},
		T: dataToCodeStr,
		E: errors,
	}
}

func ReplaceGenericMessage(original string) string {
	return strings.ReplaceAll(original, "regular expression mismatch", "invalid data in field")
}

func CreateArrayOfString(strs string) []string {
	strArray := strings.Split(strs, ",")
	for i, str := range strArray {
		strArray[i] = strings.TrimLeft(str, " ")
		strArray[i] = ReplaceGenericMessage(strArray[i])
	}
	return strArray
}
