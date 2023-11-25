package com.example.Klouud.AgodaJson.Enitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "scrunchCitiesV2")
public class ScrunchCitiesV2 {

    @Id
    private String id;
    private String city;
    private String country;

}
