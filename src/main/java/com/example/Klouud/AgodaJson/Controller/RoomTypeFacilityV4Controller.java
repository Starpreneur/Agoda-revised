package com.example.Klouud.AgodaJson.Controller;

import com.example.Klouud.AgodaJson.Service.RoomFacilityV4Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roomFacilityController")
public class RoomTypeFacilityV4Controller {

    @Autowired
    RoomFacilityV4Service roomFacilityV4Service;

    @PostMapping("/saveRoomFacilities/{pageNumber}/{pageSize}")
    public ResponseEntity saveRoomFacilities(@PathVariable Integer pageNumber,@PathVariable Integer pageSize){
      return roomFacilityV4Service.saveRoomFacility(pageNumber,pageSize);
    }
}
