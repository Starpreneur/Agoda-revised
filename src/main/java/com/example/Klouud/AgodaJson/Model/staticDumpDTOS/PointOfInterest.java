package com.example.Klouud.AgodaJson.Model.staticDumpDTOS;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointOfInterest {

    private String nameOfAttraction;
    private String distanceInKm;
    private String description;

}
