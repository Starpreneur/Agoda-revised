package com.example.Klouud.AgodaJson.Model.staticDumpDTOS;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Amenity {

    private String name;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
