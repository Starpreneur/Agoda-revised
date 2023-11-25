package com.example.Klouud.AgodaJson.Model.OtaDTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deal {

    private Integer id;
    private Integer value;
    private Boolean applicable;
    private String title;
    private String description;
    private String type;
    private BigDecimal discountAmount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();
    @JsonProperty(value = "BookingScope")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String bookingScope;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String roomId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ratePlanId;
}
