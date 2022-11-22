package dtos

type DataToCode struct {
	K  string `json:"K"`
	V  string `json:"V"`
	C  string `json:"C"`
	R  string `json:"R" validate:"regexp=[A-Za-z0-9]{30}"`
	N  string `json:"N" validate:"regexp=[A-Za-z0-9]{30}"`
	I  string `json:"I" validate:"regexp=RSD[1-9]{30}"`
	SF string `json:"SF"`
}
