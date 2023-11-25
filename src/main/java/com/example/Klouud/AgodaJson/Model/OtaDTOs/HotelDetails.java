package com.example.Klouud.AgodaJson.Model.OtaDTOs;

import com.example.Klouud.AgodaJson.Enum.CommissionType;
import com.example.Klouud.AgodaJson.Enum.OTA;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDetails {


    private String hotelId;

    private String hotelName;


    private BigDecimal lowestPrice;


    private BigDecimal minPrice;


    private BigDecimal maxPrice;


    private BigDecimal priceWithoutCommission;


    private OTA ota;


    private String roomId;


    private String ratePlanId;

    private BigDecimal baseFare;

    private BigDecimal tax;

    private BigDecimal discount;

    private Deal deal;

    private BigDecimal fee;

    private BigDecimal commission;

    private CommissionType commissionType;

  //  private TaReview taReview;

    private String currencyCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String buyerId;

    private String buyerKey;

    private Integer starRating;

  //  private HotelRatingResponse hotelRatings;

    private String sessionCode;

    private String cacheId;
}
