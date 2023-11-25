package com.example.Klouud.AgodaJson.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreCheckReq {

    private PrecheckDetails precheckDetails;

    @Data
    public static class PrecheckDetails{

        private Long searchId;
        private String tag;
        private Boolean allowDuplication;
        private String checkIn;
        private String checkOut;
        private Property property;
        private String language;
        private String userCountry;
        private String platform;
        private String ratePlan;

        @Data
        public static class Property{

           private Integer propertyId;
           private List<Rooms> rooms;

           @Data
           public static class Rooms{
               private String currency;
               private String paymentModel;
               private String blockId;
               private Integer count;
               private Integer adults;
               private  Integer children;
//               @JsonIgnore
//               private String bookNowPayLaterDate;
               private List<Integer> childrenAges;
               private Rate rate;
               private List<Surcharges> surcharges;
               @Data
                public static class Rate{
                    private Double inclusive;
                }

                @Data
                public static class Surcharges{
                   private Long id;
                   private Rate rate;

                   @Data
                    public static class Rate{
                       private Double inclusive;
                    }
                }

            }
        }
    }
}
