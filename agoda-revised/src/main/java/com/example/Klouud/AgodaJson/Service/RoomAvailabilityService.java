package com.example.Klouud.AgodaJson.Service;

import com.example.Klouud.AgodaJson.Enitity.RoomAvailabilitySessionDetails;
import com.example.Klouud.AgodaJson.Model.RoomAvailabilityRequestV2;
import com.example.Klouud.AgodaJson.Model.RoomAvailabilityResponseV2;
import com.example.Klouud.AgodaJson.Repository.RoomAvailabilitySessionDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class RoomAvailabilityService {

    @Autowired
    private RoomAvailabilitySessionDetailsRepository roomAvailabilitySessionDetailsRepository;

    public void buildRoomAvailabilitySessionDetailsAndSave(RoomAvailabilityRequestV2 roomAvailabilityRequestV2,
                                                           RoomAvailabilityResponseV2 roomAvailabilityResponseV2,
                                                                              Object otaRequest,Object otaResponse){

        RoomAvailabilitySessionDetails roomAvailabilitySessionDetails = new RoomAvailabilitySessionDetails();
        //Set Id of Room Availability Session Details as hotel Session Id
        roomAvailabilitySessionDetails.setId(roomAvailabilityResponseV2.getHotelSessionId());
        roomAvailabilitySessionDetails.setOta(roomAvailabilityResponseV2.getOta());
        //set roomAvailabilityRequestV2 in clientRequest
        roomAvailabilitySessionDetails.setClientRequest(roomAvailabilityRequestV2);
        roomAvailabilitySessionDetails.setClientResponse(roomAvailabilityResponseV2);
        //set propertySearchRequest in otaRequest
        roomAvailabilitySessionDetails.setOtaRequest(otaRequest);
        roomAvailabilitySessionDetails.setOtaResponse(otaResponse);
        roomAvailabilitySessionDetails.setCreatedDateTime(LocalDateTime.now());

        roomAvailabilitySessionDetailsRepository.save(roomAvailabilitySessionDetails);

    }

    public RoomAvailabilitySessionDetails getRoomSessionDetails(String sessionId){

        Optional<RoomAvailabilitySessionDetails> roomAvailabilitySessionDetails = roomAvailabilitySessionDetailsRepository.findById(sessionId);
        if(roomAvailabilitySessionDetails.isPresent()){
            return roomAvailabilitySessionDetails.get();
        }
        else
        {
            return null;
        }
    }
}
