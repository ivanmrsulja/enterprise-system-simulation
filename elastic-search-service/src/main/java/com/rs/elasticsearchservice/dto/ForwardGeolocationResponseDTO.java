package com.rs.elasticsearchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForwardGeolocationResponseDTO {

    private Double lat;

    private Double lon;
}
