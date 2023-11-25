package com.example.Klouud.AgodaJson.Model;


import com.example.Klouud.AgodaJson.Enum.OTA;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomAvailabilityResponseV2 {

    private String hotelSessionId;
    private OTA ota;
    private String hotelId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;
    private List<Rooms> rooms = new ArrayList<>();
    private String hotelRemarks;
    private String hotelContactNumber;
    private String hotelEmailId;
    private String hotelContactPerson;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Rooms {

        private String roomSessionId;
        private String roomId;
        private String roomType;
        private Integer extraBeds;
        private Price price;
        //The below markupDetails is kept only because there may be already data with this name
        private MarkupDetails markupDetails;
        private MarkupDetails markupAndConversionDetails;
        private List<DateWisePrice> dateWisePrice = new ArrayList<>();
        private List<ApplicableAddons> applicableAddons = new ArrayList<>();
        private Deal deal;
        private MealPlan mealPlan;
        private List<String> cancellationPolicyDescription = new ArrayList<>();
        private List<CancellationPolicyDetails> cancellationPolicyDetails = new ArrayList<>();
        private Long availableQuantity;
        //        @JsonFormat(pattern="yyyy-MM-dd")
        private LocalDate cancellationExpiryDate;
        private List<String> inclusions = new ArrayList<>();
        //HOTELBEDS Specific values
        private List<String> rateCommentsId = new ArrayList<>();
        //TBO Specific Values
        private String smokingPreference;
        private List<BedTypes> bedTypes = new ArrayList<>();
        private List<Surcharges> surcharges = new ArrayList<>();
        //private AutoApplyDealDetail autoApplyDealDetails;
        //private AppliedPromo appliedPromo;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Price {

        private BigDecimal baseAmount;
        private BigDecimal discountAmount;
        private BigDecimal taxAmount;
        private BigDecimal addOnAmount;
        private BigDecimal totalAmount;
        private String currency;
        private Long totalPromoDiscount;
        private Long totalAutoApplyDealDiscount;
        private BigDecimal totalAmountBeforePromotion;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DateWisePrice {

        private String date;
        private Price price;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicableAddons {

        private Long id;
        private String name;
        private String description;
        private String chargeType;
        private String chargeScope;
        private BigDecimal charges;
        private Boolean isMandatory;
        private List<String> imageUrls = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Deal {

        private String title;
        private String description;
        private String type;
        private BigDecimal value;
        private BigDecimal discountAmount;
        private String bookingScope;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MealPlan {

        private Boolean roomOnly;
        private Boolean breakFastIncluded;
        private Boolean lunchIncluded;
        private Boolean dinnerIncluded;
        private Boolean lunchOrDinner;
        private Boolean allInclusive;
        private String mealPlanDescription;
        private Boolean selfCatering;
        private Boolean payAtHotel;

        public MealPlan(boolean roomOnly, boolean breakFastIncluded, boolean lunchIncluded, boolean dinnerIncluded,
                        boolean lunchOrDinner, boolean allInclusive, String mealPlanDescription) {
            this.roomOnly = roomOnly;
            this.breakFastIncluded = breakFastIncluded;
            this.lunchIncluded = lunchIncluded;
            this.dinnerIncluded = dinnerIncluded;
            this.lunchOrDinner = lunchOrDinner;
            this.allInclusive = allInclusive;
            this.mealPlanDescription = mealPlanDescription;
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CancellationPolicyDetails {

        private String duration_type;
        private Long from_value;
        private Long to_value;
        private String charge_type;
        private BigDecimal value;
        private String statement;
        private String association;
        private String utcTimeZone;
        private String checkInTime;
        private Integer roomNights;
    }

    @Data
    @Builder
    public static class BedTypes {

        private String id;
        private String description;
    }

    @Data
    @Builder
    public static class Surcharges {

        private String name;
        private BigDecimal exclusive;
        private BigDecimal tax;
        private BigDecimal inclusive;
        private Boolean payAtHotel;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MarkupDetails {
        private BigDecimal otaAmount;
        private String otaCurrency;
    //    private MarkupCache appliedMarkup;
        private BigDecimal markupAmount;
        private BigDecimal appliedConversionRate;
    }

}
