package com.example.Klouud.AgodaJson.Model.OtaDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomBreakUp {

    private Integer numOfAdults;
    private Integer numOfChildren;
    private List<Integer> childAges = new ArrayList<>();

}
