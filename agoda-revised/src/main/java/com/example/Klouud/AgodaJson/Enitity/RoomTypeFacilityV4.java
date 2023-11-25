package com.example.Klouud.AgodaJson.Enitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeFacilityV4 {

    private RoomTypeFacilityFeed roomTypeFacilityFeed;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoomTypeFacilityFeed {

        private RoomTypeFacilities roomTypeFacilities;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class RoomTypeFacilities{

            List<RoomTypeFacility> roomTypeFacility = new ArrayList<>();

            @AllArgsConstructor
            @NoArgsConstructor
            @Data
            public static class RoomTypeFacility{

                private Integer hotelId;
                private Integer hotelRoomTypeId;
                private Integer propertyId;
                private String propertyName;
                private String translatedName;

            }

        }
    }
}
