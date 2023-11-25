package com.example.Klouud.AgodaJson.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreCheckRes {

    private String status;
    private String message;
    private List<ErrorList> errorList;
    @Data
    public static class ErrorList {
        private Long hotelId;
        private Long roomId;
        private String uid;
        private String code;
        private String message;
    }

}
