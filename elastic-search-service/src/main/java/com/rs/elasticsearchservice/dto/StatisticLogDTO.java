package com.rs.elasticsearchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticLogDTO {

    private String city;

    private String company;

    private String user;
}
