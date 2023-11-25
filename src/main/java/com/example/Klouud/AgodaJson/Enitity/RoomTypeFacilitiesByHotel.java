package com.example.Klouud.AgodaJson.Enitity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "RoomTypeFacilitiesByHotel")
public class RoomTypeFacilitiesByHotel {

    @Id
    private String id;
    List<RoomTypeFacility> roomTypeFacilityList = new ArrayList<>();
            @AllArgsConstructor
            @NoArgsConstructor
            @Data
            @Builder
            public static class RoomTypeFacility{

                private Integer hotelId;
                private Integer hotelRoomTypeId;
                private Integer propertyId;
                private String propertyName;
                private String translatedName;

            }
}
