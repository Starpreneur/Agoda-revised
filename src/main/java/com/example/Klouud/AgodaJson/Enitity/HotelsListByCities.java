package com.example.Klouud.AgodaJson.Enitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "agodaHotelsListByCitiesV4")
public class HotelsListByCities {

    @Id
    private String id;

    private Integer cityId;

    private String cityName;

    private Integer countryId;

    private String countryName;

    List<Integer> hotelList = new ArrayList<>();
}
