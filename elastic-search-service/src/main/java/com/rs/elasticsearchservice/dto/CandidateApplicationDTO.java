package com.rs.elasticsearchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateApplicationDTO {

    private String name;

    private String surname;

    private String education;

    private String cvPath;

    private String letterPath;

    private String address;

    private Map<String, List<String>> highlights;
}
