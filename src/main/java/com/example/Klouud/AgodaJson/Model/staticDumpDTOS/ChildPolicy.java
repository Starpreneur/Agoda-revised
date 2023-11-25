package com.example.Klouud.AgodaJson.Model.staticDumpDTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildPolicy {

    private Integer infantMinAge;
    private Integer infantMaxAge;
    private  Integer childMinAge;
    private Integer childMaxAge;
    private Integer minGuestAge;
    private Boolean childStayFree;
}
