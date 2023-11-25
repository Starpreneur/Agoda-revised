package com.example.Klouud.AgodaJson.Enitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "agodaCitiesListV3")
public class Cities {

    @Id
    private String id;

    private Integer cityId;

    private String cityName;

    private Integer countryId;

    private String cityTranslated;

    private Integer activeHotels;

    private Double longitude;

    private Double latitude;

    private Integer noArea;

}



