package com.example.Klouud.AgodaJson.Enitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "agodaCountriesV4")
public class AgodaCountriesV4 {

    @Id
    private String id;
    private Integer countryId;
    private Integer continentId;
    private String countryName;
    private String countryTranslated;
    private String activeHotels;
    private String countryIso;
    private String countryIso2;
    private Double longitude;
    private Double latitude;

}
