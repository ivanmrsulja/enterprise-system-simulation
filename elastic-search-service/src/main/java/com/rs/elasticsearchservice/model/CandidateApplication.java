package com.rs.elasticsearchservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "applications")
public class CandidateApplication {

    @Id
    private String id;

    @Field(type = FieldType.Text, store = true, name = "name")
    private String name;

    @Field(type = FieldType.Text, store = true, name = "surname")
    private String surname;

    @Field(type = FieldType.Text, store = true, name = "education")
    private String education;

    @Field(type = FieldType.Text, store = true, name = "cv")
    private String cv;

    @Field(type = FieldType.Text, store = true, name = "letter")
    private String letter;

    @Field(type = FieldType.Text, store = true, name = "cvPath")
    private String cvPath;

    @Field(type = FieldType.Text, store = true, name = "letterPath")
    private String letterPath;

    @Field(type = FieldType.Text, store = true, name = "address")
    private String address;

    @GeoPointField
    @Field(store = true, name = "location")
    private GeoPoint location;

    public CandidateApplication(String name, String surname, String education, String cv, String letter, String address, GeoPoint location) {
        this.name = name;
        this.surname = surname;
        this.education = education;
        this.cv = cv;
        this.letter = letter;
        this.address = address;
        this.location = location;
    }
}
