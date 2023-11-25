package com.example.Klouud.AgodaJson.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelBookingRequestV2 {


    private String token;


    private String key;


    private String tag;
}
