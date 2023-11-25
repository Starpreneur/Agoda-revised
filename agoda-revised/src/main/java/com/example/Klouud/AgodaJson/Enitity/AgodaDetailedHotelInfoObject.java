package com.example.Klouud.AgodaJson.Enitity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "agodaDetailedInfoForHotelV4")
public class AgodaDetailedHotelInfoObject {

    @Id
    private String id;

    private Hotels hotels;
    private List<Addresses> addresses;
    private List<Descriptions> descriptions;
    private List<Facilities> facilities;
    private List<Pictures> pictures;
    private List<RoomTypes> roomtypes;

    @Data
    public static class Hotels{

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
        private String accommodationType;
        private String nationalityRestrictions;
        private ChildAndExtraBedPolicy childAndExtrabedPolicy;

        @Data
        public static class ChildAndExtraBedPolicy{
            private Integer infantAge;
            private Integer childrenAgeFrom;
            private Integer childrenAgeTo;
            private String childrenStayFree;
            private Integer minGuestAge;
        }
    }

    @Data
    public static class Addresses{

        private Integer hotelId;
        private String addressType;
        private String addressLine1;
        private String addressLine2;
        private String postalCode;
        private String state;
        private String city;
        private String country;
    }

    @Data
    public static class Descriptions{
        private Integer hotelId;
        private String overview;
        private String snippet;
    }

    @Data
    public static class Facilities{
        private Integer hotelId;
        private String propertyGroupDescription;
        private Integer propertyId;
        private String propertyName;
        private String propertyTranslatedName;
    }

    @Data
    public static class Pictures{
        private Integer hotelId;
        private Integer pictureId;
        private String caption;
        private String captionTranslated;
        @JsonProperty("URL")
        private String url;
    }

    @Data
    public static class RoomTypes{
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
