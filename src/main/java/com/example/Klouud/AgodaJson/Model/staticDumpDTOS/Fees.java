package com.example.Klouud.AgodaJson.Model.staticDumpDTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Fees {

    // Descriptions for resort fees, city taxes, specific VAT and other mandatory taxes or charges.
    // May describe which services are covered by any fees, such as fitness centers or internet access.
    private String mandatory;

    //This file contains descriptions for incidental per-room and service fees, e.g. pet fees, breakfast, room deposits, parking and shuttle fees.
    private String incidental;

}
