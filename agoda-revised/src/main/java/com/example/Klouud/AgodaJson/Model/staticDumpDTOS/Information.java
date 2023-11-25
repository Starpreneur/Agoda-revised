package com.example.Klouud.AgodaJson.Model.staticDumpDTOS;

import com.example.Klouud.AgodaJson.Enum.CardsAccepted;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Information {


    private String name;//name
    private String chainName;
    private String chainId;
    private String hotelClass;
    private String location;//city
    private String city;//city

    @Indexed
    private String country;

    @Indexed
    private String countryCode;//country_code
    private String address1;//address
    private String address2;
    private String area;
    private String usp;
    private String overview;//description
    private String whatToExpect;
    private String reviewRating;//trustyou_rating
    private String reviewCount;//trustyou_review_count
    private String latitude;//latitude
    private String longitude;//latitude

    //This field is required for mapping hotels
    private Point point;//lat,log

    private String checkInTime;//check_in_time
    private String checkOutTime;//check_out_time
    private String starRating;//rating

    private String imagePath;
    private String hotelSearchKey;
    private String areaSeoId;
    private String secondaryAreaIds;
    private String secondaryAreaName;
    private String noOfFloors;
    private String noOfRooms;
    private List<RoomType> roomTypes;
    private String state;//state_province
    private String stateCode;
    private String postalCode;//postal_code
    private String themeList;
    private String categoryList;
    private String cityZone;
    private String weekDayRank;
    private String weekEndRank;
    private Fees fees;
    private CardsAccepted cardsAccepted;
    private List<CommunicationInfo> communicationInfo;
    //Information about what kind of rooms are available at the property.
    private String roomDescription;
    private String vendorCode;
    private String commission;
    private String tripAdvisorRatingId;
    private String hotelRemarks;


}
