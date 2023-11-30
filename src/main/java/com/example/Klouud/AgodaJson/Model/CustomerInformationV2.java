package com.example.Klouud.AgodaJson.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInformationV2 {


    private String firstName;


    private String lastName;


    private String emailId;

    //"Customer's phone number. To be stored by Scrunch and NOT to be sent to OTA"
    private String mobileNumber;

    //"The customer's city name"
    private String city;

    //"The customer's state code"
    private String state;

    //"The customer's country code"
    private String country;

    //"The customer's Address"
    private String address;

    //"The customer's postal code"
    private String postalCode;

    //"The customer's age"
    private Long age;
}
