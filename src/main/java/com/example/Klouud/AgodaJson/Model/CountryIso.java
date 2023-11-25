package com.example.Klouud.AgodaJson.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Negative;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryIso {
    private String countryIso2;
}
