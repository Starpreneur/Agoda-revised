package com.example.Klouud.AgodaJson.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertyAvailabilityRes {

    private Long searchId;
    private List<Properties> properties;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Properties{

        private Integer propertyId;
        private String propertySessionId;
        private String propertyName;
        private String translatedPropertyName;
        private List<Rooms> rooms;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Rooms{

            private Integer roomId;
            private String roomSessionId;
            private String blockId;
            private String roomName;
            private String parentRoomName;
            private String translatedRoomName;
            private String blockIdBackup;
            private String parentRoomId;
            private String ratePlanId;
            private boolean freeWifi;
            private Integer remainingRooms;
            private Integer normalBedding;
            private Integer extraBeds;
            private boolean freeBreakfast;
            private boolean freeCancellation;
            private TotalPayment totalPayment;
            private boolean roomTypeNotGuaranteed;
            private String paymentModel;
            private Rate rate;
            private List<DailyRate> dailyRate;
            private PromotionDetail promotionDetail;
            private List<Surcharges> surcharges;
            private List<TaxBreakDown> taxBreakdown;
            private CancellationPolicy cancellationPolicy;
            private List<Benefits> benefits;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class TotalPayment{
                private Double exclusive;
                private Double inclusive;
                private Double tax;
                private Double fees;
                private Double taxDueSupplier;
            }
            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Rate{
                private String currency;
                private Double exclusive;
                private Double inclusive;
                private Double tax;
                private Double fees;
                private String method;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class DailyRate{
                private String date;
                private Double exclusive ;
                private Double inclusive;
                private Double tax;
                private Double fees;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class PromotionDetail{
                private Long promotionId;
                private Boolean codeEligible;
                private String description;
                private Double savingAmount;
                private Integer supplierPromoTypeId;
                private String supplierPromoTypeDescription;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Surcharges{
                private Integer id;
                private String method;
                private String charge;
                private String margin;
                private String name;
                private Rate rate;

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Rate{
                    private String currency;
                    private Double exclusive;
                    private Double inclusive;
                    private Double tax;
                    private Double fees;
                }
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class TaxBreakDown{
                private Integer id;
                private String typeValue;
                private String taxDescription;
                private String translatedTaxDescription;
                private String method;
                private String currency;
                private String base;
                private String taxable;
                private String percent;
                private String amount;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class CancellationPolicy{
                private String code;
                private String cancellationText;
                private String translatedCancellationText;
                private List<Parameter> parameter;
                private List<Date> date;

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Parameter{
                    private Integer days;
                    private String charge;
                    private Integer value;

                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Date{
                    private String before;
                    private String onward;
                    private Rate rate;
                    @Data
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Rate{
                        public Double exclusive;
                        public Double inclusive;
                        public Double tax;
                        public Double fees;
                    }
                }

            }


            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Benefits{
                private Integer id;
                private String benefitName;
                private String translatedBenefitName;
                @JsonIgnore
                private String template;
                @JsonIgnore
                private String templateTranslated;
            }

        }
    }

}
