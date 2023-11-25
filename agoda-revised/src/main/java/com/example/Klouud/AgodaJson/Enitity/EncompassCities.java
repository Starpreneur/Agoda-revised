package com.example.Klouud.AgodaJson.Enitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "cities")
public class EncompassCities {

    @Id
    private String id;

    @Indexed
    private String city;
    private String state;
    private List<String> locality=new ArrayList<>();
    private List<String> alias=new ArrayList<>();
    private String cleartripCityId;
    private String cleartripCity;
    private String travelguruCity;
    private Set<String> gtaCity=new HashSet<>();
    private String gtaArea;
    private Country country;
    private String region;
    private Instant date;
    private List<Coordinate> coordinates=new ArrayList<>();

    @Data
    public static class Coordinate {
        private String latitude;
        private String longitude;
    }

    @Data
    public static class Country {
        private String name;
        private String alpha2Code;
        private String alpha3Code;
        private String numericCode;
    }
}
