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
@Document(collection = "agodaDetailedHotelsListByCities1_50V3")
public class HotelsDetailedListByCitiesFeed {

    @Id
    private String id;

    private Integer cityId;

    private String cityName;

    private String latitude;

    private String longitude;

    List<Hotel> hotelList = new ArrayList<>();

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Hotel {

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

                private Integer noOfReviews;

                private Integer ratingAverage;

                private String accommodationType;

                private String nationalityRestrictions;

                private ChildAndExtraBedPolicy childAndExtraBedPolicy;

                @Data
                public static class ChildAndExtraBedPolicy {

                    private Integer infantAge;

                    private Integer childrenAgeFrom;

                    private Integer childrenAgeTo;

                    private String childrenStayFree;

                    private Integer minGuestAge;

                }

        }
}
