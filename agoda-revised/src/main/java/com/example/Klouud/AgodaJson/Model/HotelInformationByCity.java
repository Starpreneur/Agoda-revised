package com.example.Klouud.AgodaJson.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelInformationByCity {

    private HotelInformationFeed hotelInformationFeed;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HotelInformationFeed {

        private HotelInformations hotelInformations;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class HotelInformations {

            List<HotelInformation> hotelInformation = new ArrayList<>();

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class HotelInformation {

                private Integer hotelId;

                private String hotelName;

                private String hotelFormerlyName;

                private String translatedName;

                private String starRating;

                private String continentId;

                private String countryId;

                private String cityId;

                private String areaId;

                private String longitude;

                private String latitude;

                private String hotelUrl;

                private Integer popularityScore;

                private String remark;

                private Integer numberOfReviews;

                private Integer ratingAverage;

                private ChildAndExtraBedPolicy childAndExtrabedPolicy ;

                private String accommodationType;

                private String nationalityRestrictions;

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class ChildAndExtraBedPolicy {

                    private Integer infantAge;

                    private Integer childrenAgeFrom;

                    private Integer childrenAgeTo;

                    private String childrenStayFree;

                    private Integer minGuestAge;

                }

            }
        }
    }
}
