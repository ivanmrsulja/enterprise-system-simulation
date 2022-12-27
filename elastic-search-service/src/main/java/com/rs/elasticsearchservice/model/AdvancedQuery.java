package com.rs.elasticsearchservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedQuery {

    private List<String> expression;

    private String cityName;

    private Double distance;
}