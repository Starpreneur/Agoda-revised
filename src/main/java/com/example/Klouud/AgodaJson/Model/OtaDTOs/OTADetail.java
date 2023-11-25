package com.example.Klouud.AgodaJson.Model.OtaDTOs;

import com.example.Klouud.AgodaJson.Enum.CommissionType;
import com.example.Klouud.AgodaJson.Enum.OTA;
import com.example.Klouud.AgodaJson.Enum.SOURCE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OTADetail {
    private OTA ota;
    private SOURCE source;

    private String url;
    private String authUrl;
    private String searchUrl;
    private String provisionalBookingUrl;
    private String bookingUrl;
    private String roomAvailabilityUrl;
    private String cancelUrl;
    private String cancellationPolicyUrl;
    private String cancellationChargesUrl;
    private String staticDumpUrl;
    private String checkCancelBookingStatusUrl;
    private String bookingDetailsUrl;
    private String promoCodeUrl;
    private String fetchHotelNameUrl;
    private Boolean active = Boolean.FALSE;
    private BigDecimal commission;
    private CommissionType commissionType;
    private Instant createDate;
    private Instant updateDate;
    private String updateBookingUrl;
    private String rateCommentsUrl;
    private String registerBuyerUrl;
    private String searchByPropertyTypeUrl;
    private String fetchFiltersUrl;
    private String fetchCityMappingUrl;
    private String updatePaymentStatusUrl;
    private String fetchActivePromotionsUrl;
    private String cancelPreCheckUrl;
    private String calculateFinalPrice;
    private String fetchBasicRatingUrl;
    private String fetchExtranetStatesUrl;
    private String postBookingPolicyUrl;
    private String preBookingUrl;
    private String checkBookingStatusUrl;
    private String pushMarkupDetailsUrl;
    private String pushBookingStatusUrl;




}
