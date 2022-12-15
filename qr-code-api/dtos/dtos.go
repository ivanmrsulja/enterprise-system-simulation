package dtos

type DataToCode struct {
	K  string `json:"K" validate:"regexp=^PR$"`
	V  string `json:"V" validate:"regexp=^01$"`
	C  string `json:"C" validate:"regexp=^1$"`
	R  string `json:"R" validate:"regexp=^[a-zA-Z0-9]{30}$"`
	N  string `json:"N" validate:"regexp=^[a-zA-Z0-9\\s]{3\\,80}$"`
	I  string `json:"I" validate:"regexp=^RSD[1-9]{1}[0-9]{0\\,11}\\,[0-9]{2}$"`
	SF string `json:"SF" validate:"regexp=^[1-9]{1}[0-9]{0\\,2}$"`
	S  string `json:"S" validate:"regexp=^[a-zA-Z0-9\\s]{3\\,80}$"`
}

type DataCoded struct {
	S CodeDesc   `json:"s"`
	T string     `json:"t"`
	N DataToCode `json:"n"`
	I []byte     `json:"i"`
}

type DataValidation struct {
	S CodeDesc   `json:"s"`
	T string     `json:"t"`
	N DataToCode `json:"n"`
}

type ErrorResponse struct {
	S CodeDesc `json:"s"`
	T string   `json:"t"`
	E []string `json:"e"`
}

type CodeDesc struct {
	Code uint32 `json:"code"`
	Desc string `json:"desc"`
}
