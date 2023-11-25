package com.example.Klouud.AgodaJson.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelFullInformation {

    private HotelFullFeed hotelFullFeed;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HotelFullFeed{

        private Hotels hotels;
        private Addresses addresses;
        private Descriptions descriptions;
        private Facilities facilities;
        private Pictures pictures;
        private RoomTypes roomtypes;
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Hotels{

            List<Hotel> hotel = new ArrayList<>();

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Hotel{

                private Integer hotelId;
                private String hotelName;
                private String hotelFormerlyName;
                private String translatedName;
                private String starRating;
                private Integer continentId;
                private Integer countryId;
                private Integer cityId;
                private String areaId;
                private String longitude;
                private String latitude;
                private String hotelUrl;
                private String popularityScore;
                private String remark;
                private String numberOfReviews;
                private Integer ratingAverage;
                private ChildAndExtraBedPolicy childAndExtrabedPolicy;

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                public static class ChildAndExtraBedPolicy{
                    private Integer infantAge;
                    private Integer childrenAgeFrom;
                    private Integer childrenAgeTo;
                    private String childrenStayFree;
                    private Integer minGuestAge;
                }

                private String accommodationType;
                private String nationalityRestrictions;


            }
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Addresses{

            List<Address> address = new ArrayList<>();

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Address{

                private Integer hotelId;
                private String addressType;
                private String addressLine1;
                private String addressLine2;
                private String postalCode;
                private String state;
                private String city;
                private String country;
            }
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Data
        public static class Descriptions{

            List<Description> description = new ArrayList<>();

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Description{

                private Integer hotelId;
                private String overview;
                private String snippet;

            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Facilities{

            List<Facility> facility = new ArrayList<>();

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Facility{

                private Integer hotelId;
                private String propertyGroupDescription;
                private Integer propertyId;
                private String propertyName;
                private String propertyTranslatedName;

            }

        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Pictures{

            List<Picture> picture = new ArrayList<>();

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Picture{
                private Integer hotelId;
                private Integer pictureId;
                private String caption;
                private String captionTranslated;
                @JsonProperty("URL")
                private String url;
            }
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class RoomTypes{

            List<RoomType> roomtype = new ArrayList<>();

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class RoomType{

                private Integer hotelId;
                private String hotelRoomtypeId;
                private String standardCaption;
                private String standardCaptionTranslated;
                private Integer maxOccupancyPerRoom;
                private Integer noOfRoom;
                private Integer  sizeOfRoom;
                private Boolean roomSizeInclTerrace;
                private String views;
                private Integer maxExtrabeds;
                private Integer maxInfantInRoom;
                private String hotelRoomtypePicture;
                private String bedType;
                private String hotelMasterRoomtypeId;
                private String hotelRoomtypeAlternateName;
                private String sharedBathroom;
                private String gender;

            }
        }
    }

}
