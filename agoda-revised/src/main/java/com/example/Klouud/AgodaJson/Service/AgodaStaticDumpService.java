package com.example.Klouud.AgodaJson.Service;

import com.example.Klouud.AgodaJson.Enitity.*;
import com.example.Klouud.AgodaJson.Enum.AmenityType;
import com.example.Klouud.AgodaJson.Enum.ImageCategory;
import com.example.Klouud.AgodaJson.Enum.ImageType;
import com.example.Klouud.AgodaJson.Enum.OTA;
import com.example.Klouud.AgodaJson.Model.staticDumpDTOS.*;
import com.example.Klouud.AgodaJson.Repository.*;
import com.example.Klouud.AgodaJson.Utils.StaticDataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

@Service
@Slf4j
public class AgodaStaticDumpService {


    @Autowired
    private NewAgodaStaticDumpHotelRepository newAgodaStaticDumpHotelRepository;

    @Autowired
    private static AgodaFeedService agodaFeedService;

    @Autowired
    private static CitiesRepository citiesRepository;

    @Autowired
    private AgodaCountryV4Repository agodaCountryV4Repository;

    @Autowired
    private AgodaDetailedInfoForHotelRepository agodaDetailedInfoForHotelRepository;

    @Autowired
    private static RoomTypeFacilitiesV4Repository roomTypeFacilitiesV4Repository;

    @Autowired
    private static RoomFacilityV4Service roomFacilityV4Service;

    @Autowired
    private StaticDataUtils staticDataUtils;

    @Transactional
    public void saveStaticDumpHotel(List<AgodaDetailedHotelInfoObject> agodaDetailedHotelsInfoList
            ,HashMap<Integer,String> countriesCodeMap,HashMap<String,String> staticDumpMap) {
        log.info("-------Building Static Dump Object--------");
        long startTime = System.currentTimeMillis();
        String city = agodaDetailedHotelsInfoList.stream().filter(agodaDetailedHotelsInfoList1 -> agodaDetailedHotelsInfoList1.getHotels() != null).collect(Collectors.toList()).get(0).getHotels().getCityId().toString();
        List<StaticDumpHotel> toSave = new ArrayList<>();


        toSave = agodaDetailedHotelsInfoList.stream()
                .map(detailedHotelInfoObject -> {
                    try {
                        if(!staticDumpMap.containsKey(staticDataUtils.buildStaticDumpId(detailedHotelInfoObject.getHotels().getHotelId()))) {
                            return StaticDumpHotel.builder()
                                    .id(detailedHotelInfoObject.getHotels() != null ? buildIdForStaticDumpObject(detailedHotelInfoObject.getHotels().getHotelId().toString()) : "")
                                    .ota(OTA.AGODA)
                                    .images(setImageObject(detailedHotelInfoObject.getPictures()))
                                    .hotelAmenities(setHotelAmenities(detailedHotelInfoObject.getFacilities()))
                                    .information((detailedHotelInfoObject.getHotels() != null && detailedHotelInfoObject.getAddresses() != null && detailedHotelInfoObject.getDescriptions() != null)
                                            ? setInformation(detailedHotelInfoObject.getHotels(), detailedHotelInfoObject.getAddresses()
                                            , countriesCodeMap, detailedHotelInfoObject.getDescriptions()) : new Information())
                                    .roomInformations((detailedHotelInfoObject.getRoomtypes() != null && detailedHotelInfoObject.getHotels() != null)
                                            ? setRoomInformation(detailedHotelInfoObject.getRoomtypes(), detailedHotelInfoObject.getHotels().getHotelId()) : new ArrayList<>())
                                    .childPolicy(detailedHotelInfoObject.getHotels() != null ? setChildPolicy(detailedHotelInfoObject.getHotels().getChildAndExtrabedPolicy()) : new ChildPolicy())
                                    .build();
                        }else {
                            return null;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());

        List<StaticDumpHotel> savedList = newAgodaStaticDumpHotelRepository.saveAll(toSave);
        log.info("static dump saved for cityId " + city);
        long endTime = System.currentTimeMillis();
        log.info("Time taken to save static dump is : "+(endTime-startTime)/1000+" sec");
        log.info("-------Static Dump Building Completed---------");
    }

    public static String buildIdForStaticDumpObject(String id) {
        long startTime = System.currentTimeMillis();
        if (id != null && !id.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append(id);
            builder.append("_");
            builder.append(OTA.AGODA);
            String idToSet = builder.toString();
            long endTime = System.currentTimeMillis();
   //         log.info("Time to build id for static dump : "+(endTime-startTime)/1000+" sec");
            return idToSet;
        } else
            return "";
    }


    public static HashMap<Integer, String> countriesCodeMap(List<AgodaCountriesV4> countriesV4List) {
        Map<Integer, String> countriesCodeMap = countriesV4List.stream().collect(Collectors.toMap(AgodaCountriesV4::getCountryId, AgodaCountriesV4::getCountryIso2));
        return (HashMap<Integer, String>) countriesCodeMap;
    }

    public List<AgodaCountriesV4> getCountriesList() {
        List<AgodaCountriesV4> countriesV4List = agodaCountryV4Repository.findAll();
        return countriesV4List;
    }


    //Method to set List of Image Object
    public static List<Image> setImageObject(List<AgodaDetailedHotelInfoObject.Pictures> picturesList) {

        long sTime = System.currentTimeMillis();
        //This is the image list to be returned
        List<Image> toReturn = new ArrayList<>();
        if (!picturesList.isEmpty()) {
            toReturn = picturesList.stream().map(pictures -> {
                //this is the Image object to be added in toReturn object
                return Image.builder().defaultImage(pictures.getCaption())
                        .url(pictures.getUrl())
                        .type(ImageType.UNKNOWN)
                        .imageCategory(ImageCategory.PROPERTY).build();

            }).collect(Collectors.toList());
        }
        long eTime = System.currentTimeMillis();
   //     log.info("Time taken to form Image object : "+(eTime-sTime)/1000+" sec");
        return toReturn;
    }

    //Method to set list of Hotel Amenities
    public static List<HotelAmenity> setHotelAmenities(List<AgodaDetailedHotelInfoObject.Facilities> facilitiesList) {

        List<HotelAmenity> toReturn = new ArrayList<>();
        if (!facilitiesList.isEmpty()) {
            toReturn = facilitiesList.stream().map(facilities -> {
                return HotelAmenity.builder()
                        .amenityType(AmenityType.PROPERTY)
                        .name(facilities.getPropertyName())
                        .description(facilities.getPropertyName())
                        .category(facilities.getPropertyGroupDescription()).build();

            }).collect(Collectors.toList());
        }
        return toReturn;
    }

    //Method to set Information Object
    public static Information setInformation(AgodaDetailedHotelInfoObject.Hotels hotels
            , List<AgodaDetailedHotelInfoObject.Addresses> addresses, HashMap<Integer, String> countriesCodeMap
            , List<AgodaDetailedHotelInfoObject.Descriptions> descriptions) {

        if (hotels != null && addresses != null && !addresses.isEmpty() && descriptions != null && !descriptions.isEmpty()) {
            long startTime = System.currentTimeMillis();
            Information toReturn = new Information();
            toReturn.setCity(addresses.get(0).getCity());
            toReturn.setLatitude(hotels.getLatitude());
            toReturn.setLongitude(hotels.getLongitude());
            toReturn.setName(hotels.getHotelName());
            toReturn.setLocation(addresses.get(0).getCity());
            toReturn.setAddress1(addresses.get(0).getAddressLine1());
            toReturn.setAddress2(addresses.get(0).getAddressLine2());
            toReturn.setPostalCode(addresses.get(0).getPostalCode());
            toReturn.setState(addresses.get(0).getState());
            toReturn.setHotelRemarks(hotels.getRemark());
            toReturn.setCountry(addresses.get(0).getCountry());
            toReturn.setCountryCode(countriesCodeMap.get(hotels.getCountryId()));
            toReturn.setStarRating(hotels.getStarRating() != null ? hotels.getStarRating() : null);
            toReturn.setOverview(descriptions.get(0).getOverview());
            long endTime = System.currentTimeMillis();
  //          log.info("Time to form information object : "+(endTime-startTime)/1000+" sec");
            return toReturn;
        } else {
            return new Information();
        }
    }

    //Set Room Information List
    public static List<RoomInformation> setRoomInformation(List<AgodaDetailedHotelInfoObject.RoomTypes> roomTypesList, Integer hotelId) throws IOException {

            List<RoomInformation> toReturn = new ArrayList<>();
        long sTime = System.currentTimeMillis();
            //collect all roomFacilities of the hotel here for ingesting in roomAmenities attribute of roomInformation
        try {
            if (roomTypesList != null && !roomTypesList.isEmpty()) {

                RoomTypeFacilitiesByHotel roomTypeFacilitiesByHotel = fetchRoomTypeFacilitiesForHotel(hotelId) ;
                RoomTypeFacilityV4 roomTypeFacilityV4 = getRoomTypeFacilities(hotelId);
                toReturn = roomTypesList.stream().map(roomTypes ->
                {

                    return RoomInformation.builder().roomTypeId(roomTypes.getHotelRoomtypeId())

                            .roomType(roomTypes.getStandardCaption())
                            .description(roomTypes.getStandardCaption())
                            .maxAdultOccupancy("")
                            .maxChildOccupancy("")
                            .maxInfantOccupancy(roomTypes.getMaxInfantInRoom().toString() + "")
                            .maxGuestOccupancy(roomTypes.getMaxOccupancyPerRoom().toString() + "")
                            .roomTypeImagePath(roomTypes.getHotelRoomtypePicture())
                            .view(roomTypes.getViews())
                            .bedType(roomTypes.getBedType())
                            .amenities(setRoomTypeAmenities(roomTypeFacilitiesByHotel, roomTypes.getHotelRoomtypeId()))
                            .build();

                }).collect(Collectors.toList());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        long eTime = System.currentTimeMillis();
 //       log.info("Time to form room Information object : "+(eTime-sTime)/1000+" sec");
            return toReturn;



    }

    public static RoomTypeFacilitiesByHotel fetchRoomTypeFacilitiesForHotel(Integer hotelId){
        StringBuilder builder = new StringBuilder();
        builder.append(hotelId.toString()).append("_").append("AGODA");
        String id = builder.toString();
        Optional<RoomTypeFacilitiesByHotel> optional = roomTypeFacilitiesV4Repository.findById(id);
        RoomTypeFacilitiesByHotel toReturn = new RoomTypeFacilitiesByHotel();
        if(optional.isPresent()) {
            toReturn = optional.get();
        }
        return toReturn;
    }


    //Set Child Policy
    public static ChildPolicy setChildPolicy(AgodaDetailedHotelInfoObject.Hotels.ChildAndExtraBedPolicy childAndExtraBedPolicy) {

        ChildPolicy toReturn = new ChildPolicy();
        long sTime = System.currentTimeMillis();
        if (childAndExtraBedPolicy != null) {
            toReturn.setInfantMinAge(0);
            toReturn.setChildMaxAge(childAndExtraBedPolicy.getChildrenAgeTo());
            toReturn.setChildMinAge(childAndExtraBedPolicy.getChildrenAgeFrom());
            toReturn.setInfantMaxAge(childAndExtraBedPolicy.getInfantAge());
            toReturn.setChildStayFree(Boolean.valueOf(childAndExtraBedPolicy.getChildrenStayFree()));
            toReturn.setMinGuestAge(childAndExtraBedPolicy.getMinGuestAge());
        }
        long eTime = System.currentTimeMillis();
  //      log.info("Time taken to form child policy object : "+(eTime-sTime)/1000+" sec");
        return toReturn;
    }


    public static Map<String, Object> setAdditionalPropertiesForRoomInformation(AgodaDetailedHotelInfoObject.RoomTypes roomTypes) {
        Map<String, Object> map = new HashMap<>();
        map.put("roomTypeImagePath", roomTypes.getHotelRoomtypePicture());
        return map;
    }


    //Service to get static dump
    public StaticDumpHotel fetchStaticDumpHotel(String hotelId) {
        // String id = buildIdForStaticDumpObject(hotelId);
        Optional<StaticDumpHotel> optionalNewAgodaStaticDumpHotel = newAgodaStaticDumpHotelRepository.findById(hotelId);
        if (optionalNewAgodaStaticDumpHotel.isPresent()) {
            StaticDumpHotel toReturn = new StaticDumpHotel();
            StaticDumpHotel staticDumpHotel = optionalNewAgodaStaticDumpHotel.get();
            toReturn.setId(staticDumpHotel.getId());
            toReturn.setOta(OTA.AGODA);
            toReturn.setImages(staticDumpHotel.getImages());
            toReturn.setHotelPolicies(staticDumpHotel.getHotelPolicies());
            toReturn.setInformation(staticDumpHotel.getInformation());
            toReturn.setHotelAmenities(staticDumpHotel.getHotelAmenities());
            toReturn.setChildPolicy(staticDumpHotel.getChildPolicy());
            toReturn.setRoomAmenities(staticDumpHotel.getRoomAmenities());
            toReturn.setRenovationDetails(staticDumpHotel.getRenovationDetails());
            toReturn.setUpdateDate(staticDumpHotel.getUpdateDate());
            toReturn.setRoomInformations(staticDumpHotel.getRoomInformations());
            toReturn.setPointsOfInterest(staticDumpHotel.getPointsOfInterest());
            return toReturn;
        } else {
            return new StaticDumpHotel();
        }
    }

    public StaticDumpHotel updateStaticDumpRoomAmenitiesByHotel(String hotelId) throws IOException {

        //Fetch staticDump for particular hotel
        Optional<StaticDumpHotel> staticDumpHotelOptional = newAgodaStaticDumpHotelRepository.findById(hotelId);
        String[] hid = hotelId.split("_");
        Integer mhotel_id = Integer.valueOf(hid[0]);

        //Fetch RoomFacility list for that particular Hotel
        RoomTypeFacilityV4 roomTypeFacilityV4 = agodaFeedService.getRoomTypeFacilities(mhotel_id);

        if (staticDumpHotelOptional.isPresent()) {
            StaticDumpHotel staticDumpHotel = staticDumpHotelOptional.get();
            List<RoomInformation> roomInformationList = new ArrayList<>();
            //Stream over roomInformation list ,for each roomInformation set a list of room amenities
            staticDumpHotel.getRoomInformations().stream().forEach(roomInformation -> {
                List<RoomAmenity> roomAmenityList = new ArrayList<>();

                //for each roomInformation object traverse over roomFacility list , filter roomfacility object according to roomTypeId
                roomTypeFacilityV4.getRoomTypeFacilityFeed().getRoomTypeFacilities().getRoomTypeFacility().stream()
                        .forEach(roomTypeFacility -> {
                            if (roomTypeFacility.getHotelRoomTypeId().equals(Integer.valueOf(roomInformation.getRoomTypeId()))) {
                                RoomAmenity roomAmenity = new RoomAmenity();
                                roomAmenity.setName(roomTypeFacility.getPropertyName());
                                roomAmenity.setDescription(roomTypeFacility.getPropertyName());
                                roomAmenity.setRoomId(roomInformation.getRoomTypeId());
                                roomAmenity.setType(AmenityType.ROOM);
                                roomAmenityList.add(roomAmenity);
                            }
                        });
                roomInformation.setAmenities(roomAmenityList);
                roomInformationList.add(roomInformation);
            });
            staticDumpHotel.setRoomInformations(roomInformationList);
            StaticDumpHotel toReturn = newAgodaStaticDumpHotelRepository.save(staticDumpHotel);
            return toReturn;
        } else {
            return null;
        }
    }

    public static List<RoomAmenity> setRoomTypeAmenities(RoomTypeFacilitiesByHotel roomTypeFacilitiesByHotel,String roomTypeId) {
        try {
            if(roomTypeFacilitiesByHotel != null
                    && roomTypeFacilitiesByHotel.getRoomTypeFacilityList() != null) {
                List<RoomAmenity> roomAmenityList = new ArrayList<>();
                List<RoomTypeFacilitiesByHotel.RoomTypeFacility> roomTypeFacilityList = roomTypeFacilitiesByHotel.getRoomTypeFacilityList().stream()
                        .filter(roomTypeFacility -> roomTypeFacility.getHotelRoomTypeId().equals(Integer.valueOf(roomTypeId))).collect(Collectors.toList());  ;
                roomAmenityList = roomTypeFacilityList.stream()
                        .map(roomTypeAmenities -> {

                            return RoomAmenity.builder()
                                    .name(roomTypeAmenities.getPropertyName())
                                    .description(roomTypeAmenities.getPropertyName())
                                    .roomId(roomTypeAmenities.getHotelRoomTypeId().toString())
                                    .type(AmenityType.ROOM)
                                    .build();

                        }).collect(Collectors.toList());

                return roomAmenityList;
            }else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static RoomTypeFacilityV4 getRoomTypeFacilities(Integer hotelId) throws IOException{
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://affiliatefeed.agoda.com/datafeeds/feed/getfeed";
        String apiKey = "EA32194E-CFD5-4786-A2D4-8315EE83D1DD";
        Integer feedId = 14;
        Integer mhotel_Id = hotelId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("apiKey",apiKey)
                .queryParam("feed_id",feedId)
                .queryParam("mhotel_id",mhotel_Id);

        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,entity,byte[].class);
        byte[] roomTypeFacilityArray = response.getBody();

        ByteArrayInputStream bis = new ByteArrayInputStream(roomTypeFacilityArray);
        GZIPInputStream gis = new GZIPInputStream(bis);

        InputStreamReader reader = new InputStreamReader(gis);

        ObjectMapper objectMapper1 = new ObjectMapper();
        RoomTypeFacilityV4 roomTypeFacilityV4 = objectMapper1.readValue(reader,RoomTypeFacilityV4.class);

        return roomTypeFacilityV4;
    }
}
