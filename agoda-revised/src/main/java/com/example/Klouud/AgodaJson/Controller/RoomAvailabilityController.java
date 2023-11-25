package com.example.Klouud.AgodaJson.Controller;

import com.example.Klouud.AgodaJson.Enitity.RoomAvailabilitySessionDetails;
import com.example.Klouud.AgodaJson.Service.RoomAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoomAvailabilityController {

    @Autowired
    RoomAvailabilityService roomAvailabilityService;

    @PostMapping("/getRoomSessionDetails")
    public ResponseEntity getRoomSessionDetails(@RequestBody String sessionId){
        RoomAvailabilitySessionDetails roomAvailabilitySessionDetails = roomAvailabilityService.getRoomSessionDetails(sessionId);
        if(roomAvailabilitySessionDetails != null){
            return new ResponseEntity(roomAvailabilitySessionDetails, HttpStatus.OK);
        }
        else
            return new ResponseEntity(null,HttpStatus.OK);

    }
}
