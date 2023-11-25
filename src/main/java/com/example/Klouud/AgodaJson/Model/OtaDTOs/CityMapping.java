package com.example.Klouud.AgodaJson.Model.OtaDTOs;

import com.example.Klouud.AgodaJson.Enitity.AgodaCountries;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class CityMapping {

    @Id
    private String id;

    @Indexed
    private String city;
    private String state;
    private List<String> locality;
    private List<String> alias;
    private String cleartripCityId;
    private String cleartripCity;
    private String travelguruCity;
    private Set<String> gtaCity;
    private String gtaArea;
    private AgodaCountries.CountryFeed.Countries.Country country;
    private String region;
    private List<Coordinate> coordinates;
    private Instant date;

    public Set<String> getGtaCity() {
        if(gtaCity == null) {
            gtaCity = new HashSet<>();
        }
        return gtaCity;
    }

    @Tolerate
    public CityMapping() {
    }
}
