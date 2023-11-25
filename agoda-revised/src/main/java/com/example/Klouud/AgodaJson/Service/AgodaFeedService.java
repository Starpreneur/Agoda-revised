package com.example.Klouud.AgodaJson.Service;

import com.example.Klouud.AgodaJson.Enitity.*;
import com.example.Klouud.AgodaJson.Enum.OTA;
import com.example.Klouud.AgodaJson.Enum.Platform;
import com.example.Klouud.AgodaJson.Enum.RatePlan;
import com.example.Klouud.AgodaJson.Model.*;
import com.example.Klouud.AgodaJson.Model.OtaDTOs.*;
import com.example.Klouud.AgodaJson.Repository.*;
import com.example.Klouud.AgodaJson.Utils.AgodaAPIConstants;
import com.example.Klouud.AgodaJson.Utils.LogUtils;
import com.example.Klouud.AgodaJson.Utils.RandomUtils;
import com.example.Klouud.AgodaJson.Utils.StaticDataUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

@Service
@Slf4j
public class AgodaFeedService {

//    @Value("${listPartition.value}")
//    Integer listPartitionValue;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StaticDataUtils staticDataUtils;

    @Autowired
    private HotelIdsListByCityCacheV1Repository cityCacheV1Repository;

    @Autowired
    private CityHotelIdsCacheRepository cityHotelIdsCacheRepository;

    @Autowired
    HotelsListByCitiesFeedRepository hotelsListByCitiesFeedRepository;

    @Autowired
    AgodaDetailedInfoForHotelRepository agodaDetailedInfoForHotelRepository;

    @Autowired
    CitiesRepository citiesRepository;
    @Autowired
    AgodaHotelsListByCitiesV4Repository agodaHotelsListByCitiesV4Repository;

    @Autowired
    AgodaCountryV4Repository agodaCountryV4Repository;

    @Autowired
    RoomAvailabilityService roomAvailabilityService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private AgodaStaticDumpService agodaStaticDumpService;

    @Autowired
    LogUtils logUtils;

    Logger  LOGGER = LogManager.getLogger(AgodaFeedService.class);

    public CityFeedInfo getCitiesList() throws IOException {

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://affiliatefeed.agoda.com/datafeeds/feed/getfeed";
        String apiKey = "EA32194E-CFD5-4786-A2D4-8315EE83D1DD";
        Integer feedId = 3;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("feed_id", feedId)
                .queryParam("apikey", apiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> cityFeedResponseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, byte[].class);
        byte[] cityFeedInfo = cityFeedResponseEntity.getBody();
        System.out.println(cityFeedResponseEntity.getHeaders());

        ByteArrayInputStream bis = new ByteArrayInputStream(cityFeedInfo);
        GZIPInputStream gis = new GZIPInputStream(bis);

        InputStreamReader reader = new InputStreamReader(gis);


        ObjectMapper objectMapper = new ObjectMapper();
        CityFeedInfo cityFeedInfo1 = objectMapper.readValue(reader, CityFeedInfo.class);
        System.out.println(cityFeedInfo1);
        return cityFeedInfo1;
    }

    //service to fetch roomTypeFacilities from Agoda
    public RoomTypeFacilityV4 getRoomTypeFacilities(Integer hotelId) throws IOException{
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
        ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,entity,byte[].class);
        byte[] roomTypeFacilityArray = response.getBody();

        ByteArrayInputStream bis = new ByteArrayInputStream(roomTypeFacilityArray);
        GZIPInputStream gis = new GZIPInputStream(bis);

        InputStreamReader reader = new InputStreamReader(gis);

        ObjectMapper objectMapper1 = new ObjectMapper();
        RoomTypeFacilityV4 roomTypeFacilityV4 = objectMapper1.readValue(reader,RoomTypeFacilityV4.class);

        return roomTypeFacilityV4;
    }

    public AgodaCountries getCountriesListFromAgoda() throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://affiliatefeed.agoda.com/datafeeds/feed/getfeed";
        String apiKey = "EA32194E-CFD5-4786-A2D4-8315EE83D1DD";
        Integer feedId = 2;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("apiKey",apiKey)
                .queryParam("feed_id",feedId);


        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,entity,byte[].class);

        byte[] countryListByteArray = response.getBody();

        ByteArrayInputStream bis = new ByteArrayInputStream(countryListByteArray);
        GZIPInputStream gis = new GZIPInputStream(bis);

        InputStreamReader reader = new InputStreamReader(gis);

        ObjectMapper objectMapper = new ObjectMapper();
        AgodaCountries countries = objectMapper.readValue(reader,AgodaCountries.class);

        return countries;
    }



    public ResponseEntity saveCountriesListV4() throws IOException {
        List<AgodaCountriesV4> countriesV4List = new ArrayList<>();

        AgodaCountries agodaCountries = getCountriesListFromAgoda();

        agodaCountries.getCountryFeed().getCountries().getCountry().stream().forEach(country -> {
            AgodaCountriesV4 countriesV4 = new AgodaCountriesV4();
            countriesV4.setCountryId(country.getCountryId());
            countriesV4.setCountryIso(country.getCountryIso());
            countriesV4.setCountryName(country.getCountryName());
            countriesV4.setCountryIso2(country.getCountryIso2());
            countriesV4.setCountryTranslated(country.getCountryTranslated());
            countriesV4.setLatitude(country.getLatitude());
            countriesV4.setLongitude(country.getLongitude());
            countriesV4.setActiveHotels(country.getActiveHotels());
            countriesV4.setContinentId(country.getContinentId());
            countriesV4List.add(countriesV4);
        });

        List<AgodaCountriesV4> savedList = agodaCountryV4Repository.saveAll(countriesV4List);

        return new ResponseEntity(savedList,HttpStatus.OK);
    }

    public ResponseEntity saveCitiesListV3() throws IOException {
        List<Cities> listToSave = new ArrayList<>();
        CityFeedInfo cityFeedInfo = new CityFeedInfo();

        cityFeedInfo = getCitiesList();

        cityFeedInfo.getCityFeed().getCities().getCity().stream().forEach(city -> {
            Cities cityToSave = new Cities();
            cityToSave.setCityId(city.getCityId());
            cityToSave.setCityName(city.getCityName());
            cityToSave.setLatitude(city.getLatitude());
            cityToSave.setLongitude(city.getLongitude());
            cityToSave.setActiveHotels(city.getActiveHotels());
            cityToSave.setCountryId(city.getCountryId());
            cityToSave.setCityTranslated(city.getCityTranslated());
            cityToSave.setNoArea(city.getNoArea());

            listToSave.add(cityToSave);
        });
        List<Cities> savedList = citiesRepository.saveAll(listToSave);
        return new ResponseEntity(savedList,HttpStatus.OK);
    }


    public ResponseEntity saveHotelListsByCityBetween(Integer cityId1,Integer cityId2) {

            long startTime = System.currentTimeMillis();
            Pageable pageableCities = PageRequest.of(0,200);
            Page<Cities> citiesRecords = citiesRepository.findAll(pageableCities);
            citiesRecords.stream().forEach(city -> {

            });
            HotelIdsListByCityCacheV1 cacheV1 = new HotelIdsListByCityCacheV1();
            Map<Integer, String> countryLookUp = new HashMap<>();
            List<AgodaCountriesV4> countriesList = agodaCountryV4Repository.findAll();
            for (AgodaCountriesV4 countriesV4 : countriesList) {
                countryLookUp.put(countriesV4.getCountryId(), countriesV4.getCountryName());
            }
            List<HotelIdsListByCityCacheV1.CitiesSavedInSystem> citiesSavedInSystemList = new ArrayList<>();
            try{
            List<Cities> listOfCities = citiesRepository.findByCityIdBetween(cityId1, cityId2);
            log.info("Number of cities to save : "+listOfCities.size());
            cacheV1.setStartCityId(cityId1);
            cacheV1.setEndCityId(cityId2);
            cacheV1.setTotalNumberOfCitiesSentInRequest(listOfCities.size());
            List<List<Cities>> partitionedList = Lists.partition(listOfCities, 10);
            Integer batch = 1;
            for (List<Cities> cityListWithTenCities : partitionedList) {
                HotelIdsListByCityCacheV1.CitiesSavedInSystem citiesSavedInSystem = new HotelIdsListByCityCacheV1.CitiesSavedInSystem();
                List<HotelsListByCities> listToSave = new ArrayList<>();
                cityListWithTenCities.stream().forEach(city -> {
                    Integer cityId = city.getCityId();
                    HotelsListByCities hotelsListByCities = new HotelsListByCities();
                    hotelsListByCities.setCityId(cityId);
                    hotelsListByCities.setCityName(city.getCityName());
                    Integer countryId = city.getCountryId();
                    String countryName = countryLookUp.get(countryId);
                    hotelsListByCities.setCountryName(countryName);
                    List<Integer> hotelIdsWithInCity = new ArrayList<>();
                    try {
                        HotelInformationByCity hotelInformationByCity = getHotelsListByCity(cityId);
                        if (hotelInformationByCity.getHotelInformationFeed().getHotelInformations() != null) {
                            hotelInformationByCity.getHotelInformationFeed().getHotelInformations().getHotelInformation().stream().forEach(hotelInformation -> {
                                hotelIdsWithInCity.add(hotelInformation.getHotelId());
                            });
                            hotelsListByCities.setHotelList(hotelIdsWithInCity);

                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    listToSave.add(hotelsListByCities);
                });
                List<HotelsListByCities> savedList = agodaHotelsListByCitiesV4Repository.saveAll(listToSave);
                List<Integer> cityIds = listToSave.stream().map(cities1 -> cities1.getCityId()).collect(Collectors.toList());
                citiesSavedInSystem.setBatchNumber(batch);
                batch++;
                citiesSavedInSystem.setCityIdsSavedList(cityIds);
                citiesSavedInSystemList.add(citiesSavedInSystem);
                log.info("Hotel list saved for cityIds : " + cityIds);
            }

            return new ResponseEntity("Data saved", HttpStatus.OK);
        }finally {
                cacheV1.setCitiesSavedInSystemList(citiesSavedInSystemList);
                cityCacheV1Repository.save(cacheV1);
                long endTime = System.currentTimeMillis();
                long timeTaken = (endTime - startTime) / 1000;
                log.info("Time Taken in process :" + timeTaken + " sec");
        }
    }


    public ResponseEntity saveHotelIdsListByCitiesV2(Integer pageNumber, int pageSize)
    {
        long startTime = System.currentTimeMillis();
        Map<Integer, String> countryLookUp = setCountryLookUp();
        Pageable pageable = PageRequest.of(0,pageSize);
        Page<Cities> all = citiesRepository.findAll(pageable);
        for(int i=pageNumber ; i<=all.getTotalPages(); i++){
             pageable = PageRequest.of(i,pageSize);
            Page<Cities> citiesRecords = citiesRepository.findAll(pageable);
            HotelIdsListByCityCacheV1 cacheV1 = new HotelIdsListByCityCacheV1();

            List<HotelIdsListByCityCacheV1.CitiesSavedInSystem> citiesSavedInSystemList = new ArrayList<>();
            cacheV1.setStartCityId(citiesRecords.getContent().get(0).getCityId());
            cacheV1.setEndCityId(citiesRecords.getContent().get((citiesRecords.getContent().size())-1).getCityId());
            List<List<Cities>> partitionedList = Lists.partition(citiesRecords.getContent(),10);
            Integer batch = 1;
            for(List<Cities> listWithTenCities : partitionedList) {
                HotelIdsListByCityCacheV1.CitiesSavedInSystem citiesSavedInSystem = new HotelIdsListByCityCacheV1.CitiesSavedInSystem();
                List<HotelsListByCities> listToSave = new ArrayList<>();
                listWithTenCities.stream().forEach(city -> {
                HotelsListByCities hotelsListByCities = new HotelsListByCities();
                setHotelListByCitiesInfo(city,countryLookUp,hotelsListByCities);
                listToSave.add(hotelsListByCities);
                });
                try {
                    List<HotelsListByCities> savedList = agodaHotelsListByCitiesV4Repository.saveAll(listToSave);
                    List<Integer> cityIds = listToSave.stream().map(cities1 -> cities1.getCityId()).collect(Collectors.toList());
                    citiesSavedInSystem.setBatchNumber(batch);
                    batch++;
                    citiesSavedInSystem.setCityIdsSavedList(cityIds);
                    citiesSavedInSystemList.add(citiesSavedInSystem);
                    log.info("Hotel list saved for cityIds : " + cityIds);
                }finally {
                    cacheV1.setCitiesSavedInSystemList(citiesSavedInSystemList);
                    cityCacheV1Repository.save(cacheV1);
                    long eTime = System.currentTimeMillis();
                    long timeTaken = (eTime - startTime) / 1000;
                    log.info("Time Taken in process :" + timeTaken + " sec");
                }
            }
        }

        long endTime = System.currentTimeMillis();
        log.info("Time taken in execution :"+((endTime-startTime)/1000)/60+" min");
        return new ResponseEntity("Data saved", HttpStatus.OK);
    }

    public void setHotelListByCitiesInfo(Cities city,Map<Integer,String> countryLookUp,HotelsListByCities hotelsListByCities){
        hotelsListByCities.setCityId(city.getCityId());
        hotelsListByCities.setCityName(city.getCityName());
        hotelsListByCities.setCountryName(countryLookUp.get(city.getCountryId()));
        hotelsListByCities.setCountryId(city.getCountryId());
        hotelsListByCities.setHotelList(getHotelIdsListForCity(city.getCityId()));
    }

    public List<Integer> getHotelIdsListForCity(Integer cityId){
        List<Integer> hotelIdsWithInCity = new ArrayList<>();
        try {
            HotelInformationByCity hotelInformationByCity = getHotelsListByCity(cityId);
            if (hotelInformationByCity.getHotelInformationFeed().getHotelInformations() != null) {
                hotelInformationByCity.getHotelInformationFeed().getHotelInformations().getHotelInformation().stream().forEach(hotelInformation -> {
                    hotelIdsWithInCity.add(hotelInformation.getHotelId());
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
       return hotelIdsWithInCity;
    }
    public Map<Integer,String> setCountryLookUp(){
        Map<Integer, String> countryLookUp = new HashMap<>();
        List<AgodaCountriesV4> countriesList = agodaCountryV4Repository.findAll();
        for (AgodaCountriesV4 countriesV4 : countriesList) {
            countryLookUp.put(countriesV4.getCountryId(), countriesV4.getCountryName());
        }
        return countryLookUp;
    }


    public ResponseEntity saveHotelsByCities() throws IOException {
        long startTime = System.currentTimeMillis();
        List<HotelsDetailedListByCitiesFeed> listToSave = new ArrayList<>();
        List<Cities> cities = citiesRepository.findByCityIdBetween(1,50);

        //for every city setting list of hotels
        cities.stream().forEach(city -> {
            HotelsDetailedListByCitiesFeed hotelsListByCitiesFeed = new HotelsDetailedListByCitiesFeed();
            hotelsListByCitiesFeed.setCityId(city.getCityId());
            hotelsListByCitiesFeed.setCityName(city.getCityName());
            hotelsListByCitiesFeed.setLatitude(city.getLatitude().toString());
            hotelsListByCitiesFeed.setLongitude(city.getLongitude().toString());
            List<HotelsDetailedListByCitiesFeed.Hotel> hotelInformationList = new ArrayList<>();
            try {

                HotelInformationByCity hotelInformationByCity = getHotelsListByCity(city.getCityId());
                hotelInformationByCity.getHotelInformationFeed().getHotelInformations().getHotelInformation().stream().forEach(hotelInformation -> {
                    HotelsDetailedListByCitiesFeed.Hotel hotelInformation1 = new HotelsDetailedListByCitiesFeed.Hotel();
                    hotelInformation1.setHotelId(hotelInformation.getHotelId());
                    hotelInformation1.setHotelName(hotelInformation.getHotelName());
                    hotelInformation1.setHotelUrl(hotelInformation.getHotelUrl());
                    hotelInformation1.setCityId(hotelInformation.getCityId());
                    hotelInformation1.setLatitude(hotelInformation.getLatitude());
                    hotelInformation1.setLongitude(hotelInformation.getLongitude());
                    hotelInformation1.setHotelFormerlyName(hotelInformation.getHotelFormerlyName());
                    hotelInformation1.setAccommodationType(hotelInformation.getAccommodationType());
                    hotelInformation1.setAreaId(hotelInformation.getAreaId());
                    hotelInformation1.setHotelFormerlyName(hotelInformation.getHotelFormerlyName());
                    hotelInformation1.setContinentId(hotelInformation.getContinentId());
                    hotelInformation1.setNationalityRestrictions(hotelInformation.getNationalityRestrictions());
                    hotelInformation1.setNoOfReviews(hotelInformation.getNumberOfReviews());
                    hotelInformation1.setPopularityScore(hotelInformation.getPopularityScore());
                    hotelInformation1.setRatingAverage(hotelInformation.getRatingAverage());
                    hotelInformation1.setStarRating(hotelInformation.getStarRating());
                    hotelInformation1.setRemark(hotelInformation.getRemark());
                    hotelInformation1.setTranslatedName(hotelInformation.getTranslatedName());

                    //childAndExtraBedPolicy
                    HotelsDetailedListByCitiesFeed.Hotel.ChildAndExtraBedPolicy childAndExtraBedPolicy
                            = new HotelsDetailedListByCitiesFeed.Hotel.ChildAndExtraBedPolicy();
                    childAndExtraBedPolicy.setChildrenAgeFrom(hotelInformation.getChildAndExtrabedPolicy().getChildrenAgeFrom());
                    childAndExtraBedPolicy.setInfantAge(hotelInformation.getChildAndExtrabedPolicy().getInfantAge());
                    childAndExtraBedPolicy.setChildrenAgeTo(hotelInformation.getChildAndExtrabedPolicy().getChildrenAgeTo());
                    childAndExtraBedPolicy.setMinGuestAge(hotelInformation.getChildAndExtrabedPolicy().getMinGuestAge());
                    childAndExtraBedPolicy.setChildrenStayFree(hotelInformation.getChildAndExtrabedPolicy().getChildrenStayFree());

                    //now set child and extra bed policy
                    hotelInformation1.setChildAndExtraBedPolicy(childAndExtraBedPolicy);

                    //add hotel object in the hotel information list
                    hotelInformationList.add(hotelInformation1);
                });
                hotelsListByCitiesFeed.setHotelList(hotelInformationList);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //list for list of hotels within a city
            listToSave.add(hotelsListByCitiesFeed);
        });
        List<HotelsDetailedListByCitiesFeed> listByCitiesFeeds = hotelsListByCitiesFeedRepository.saveAll(listToSave);
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime-startTime)/1000;
        log.info("Time Taken in process :"+timeTaken+" sec");
        return new ResponseEntity(listByCitiesFeeds,HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity saveDetailedInfoForHotels(Integer pageNumber,Integer pageSize){
      log.info("------------------------------------------------------------Application Started----------------------------------------------------------------");
        long startTime = System.currentTimeMillis();
        log.info("-------------------Creating DetailedHotelInfoMap--------------------");
        Map<Integer,String> detailedHotelInfoMap = createDetailedHotelInfoMap();
        long endTimes = System.currentTimeMillis();
        log.info("Time taken to form HashMap : "+(endTimes-startTime)/1000+" seconds");
   //    List<HotelsListByCities> hotelsListByCities = agodaHotelsListByCitiesV4Repository.findByCityIdBetween(city1Id,city2Id);
       Pageable pageable = PageRequest.of(0,pageSize);
       Page<HotelsListByCities> all = agodaHotelsListByCitiesV4Repository.findAll(pageable);
       for(int i = pageNumber;i<all.getTotalPages();i++) {

           Pageable pageable1 = PageRequest.of(i,pageSize);
           Page<HotelsListByCities> hotelsListByCities = agodaHotelsListByCitiesV4Repository.findAll(pageable1);

           if (hotelsListByCities.getContent().size() != 0 && !hotelsListByCities.getContent().isEmpty()) {
               hotelsListByCities.getContent().stream().forEach(city -> {

                   List<AgodaDetailedHotelInfoObject> listToSave = new ArrayList<>();
                   //object to be added in listToSave

                   if (city.getHotelList() != null && !city.getHotelList().isEmpty()) {
                       city.getHotelList().stream().forEach(hotelId -> {
                           try {
                               if (!detailedHotelInfoMap.containsKey(hotelId)) {
                                   AgodaDetailedHotelInfoObject agodaDetailedHotelsInfo = new AgodaDetailedHotelInfoObject();
                                   HotelFullInformation hotelFullInformation = getFullHotelInformation(hotelId);

                                   if (hotelFullInformation.getHotelFullFeed().getHotels() != null && hotelFullInformation.getHotelFullFeed().getHotels().getHotel() != null &&
                                           !hotelFullInformation.getHotelFullFeed().getHotels().getHotel().isEmpty()) {
                                       //set hotel Details
                                       hotelFullInformation.getHotelFullFeed().getHotels().getHotel().stream().forEach(hotel -> {
                                           AgodaDetailedHotelInfoObject.Hotels hotels = new AgodaDetailedHotelInfoObject.Hotels();
                                           hotels.setHotelId(hotel.getHotelId());
                                           hotels.setHotelUrl(hotel.getHotelUrl());
                                           hotels.setHotelName(hotel.getHotelName());
                                           hotels.setCityId(hotel.getCityId());
                                           hotels.setAreaId(hotel.getAreaId());
                                           hotels.setLatitude(hotel.getLatitude());
                                           hotels.setLongitude(hotel.getLongitude());
                                           hotels.setHotelFormerlyName(hotel.getHotelFormerlyName());
                                           hotels.setRemark(hotel.getRemark());
                                           hotels.setContinentId(hotel.getContinentId());
                                           hotels.setAccommodationType(hotel.getAccommodationType());
                                           hotels.setNationalityRestrictions(hotel.getNationalityRestrictions());
                                           hotels.setNumberOfReviews(hotel.getNumberOfReviews());
                                           hotels.setStarRating(hotel.getStarRating());
                                           hotels.setPopularityScore(hotel.getPopularityScore());
                                           hotels.setRatingAverage(hotel.getRatingAverage());
                                           hotels.setTranslatedName(hotel.getTranslatedName());
                                           AgodaDetailedHotelInfoObject.Hotels.ChildAndExtraBedPolicy childAndExtraBedPolicy =
                                                   new AgodaDetailedHotelInfoObject.Hotels.ChildAndExtraBedPolicy();
                                           childAndExtraBedPolicy.setChildrenAgeFrom(hotel.getChildAndExtrabedPolicy().getChildrenAgeFrom());
                                           childAndExtraBedPolicy.setInfantAge(hotel.getChildAndExtrabedPolicy().getInfantAge());
                                           childAndExtraBedPolicy.setChildrenAgeTo(hotel.getChildAndExtrabedPolicy().getChildrenAgeTo());
                                           childAndExtraBedPolicy.setMinGuestAge(hotel.getChildAndExtrabedPolicy().getMinGuestAge());
                                           childAndExtraBedPolicy.setChildrenStayFree(hotel.getChildAndExtrabedPolicy().getChildrenStayFree());
                                           hotels.setChildAndExtrabedPolicy(childAndExtraBedPolicy);
                                           agodaDetailedHotelsInfo.setHotels(hotels);
                                       });
                                   }

                                   //set address list
                                   List<AgodaDetailedHotelInfoObject.Addresses> addressesList = new ArrayList<>();
                                   hotelFullInformation.getHotelFullFeed().getAddresses().getAddress().stream().forEach(address -> {
                                       AgodaDetailedHotelInfoObject.Addresses addressObject = new AgodaDetailedHotelInfoObject.Addresses();
                                       addressObject.setHotelId(address.getHotelId());
                                       addressObject.setAddressLine1(address.getAddressLine1());
                                       addressObject.setAddressLine2(address.getAddressLine2());
                                       addressObject.setAddressType(address.getAddressType());
                                       addressObject.setCity(address.getCity());
                                       addressObject.setState(address.getState());
                                       addressObject.setPostalCode(address.getPostalCode());

                                       addressesList.add(addressObject);
                                   });
                                   agodaDetailedHotelsInfo.setAddresses(addressesList);

                                   //set description list
                                   List<AgodaDetailedHotelInfoObject.Descriptions> descriptionsList = new ArrayList<>();
                                   hotelFullInformation.getHotelFullFeed().getDescriptions().getDescription().stream().forEach(description -> {
                                       AgodaDetailedHotelInfoObject.Descriptions descriptionObject = new AgodaDetailedHotelInfoObject.Descriptions();
                                       descriptionObject.setHotelId(description.getHotelId());
                                       descriptionObject.setOverview(description.getOverview());
                                       descriptionObject.setSnippet(description.getSnippet());
                                       descriptionsList.add(descriptionObject);
                                   });
                                   agodaDetailedHotelsInfo.setDescriptions(descriptionsList);


                                   //set facilities list
                                   List<AgodaDetailedHotelInfoObject.Facilities> facilitiesList = new ArrayList<>();
                                   hotelFullInformation.getHotelFullFeed().getFacilities().getFacility().stream().forEach(facility -> {
                                       AgodaDetailedHotelInfoObject.Facilities facilityObject = new AgodaDetailedHotelInfoObject.Facilities();
                                       facilityObject.setHotelId(facility.getHotelId());
                                       facilityObject.setPropertyId(facility.getPropertyId());
                                       facilityObject.setPropertyName(facility.getPropertyName());
                                       facilityObject.setPropertyGroupDescription(facility.getPropertyGroupDescription());
                                       facilityObject.setPropertyTranslatedName(facility.getPropertyTranslatedName());
                                       facilitiesList.add(facilityObject);
                                   });
                                   agodaDetailedHotelsInfo.setFacilities(facilitiesList);

                                   //set pictures list
                                   List<AgodaDetailedHotelInfoObject.Pictures> picturesList = new ArrayList<>();
                                   hotelFullInformation.getHotelFullFeed().getPictures().getPicture().stream().forEach(picture -> {
                                       AgodaDetailedHotelInfoObject.Pictures pictureObject = new AgodaDetailedHotelInfoObject.Pictures();
                                       pictureObject.setHotelId(picture.getHotelId());
                                       pictureObject.setUrl(picture.getUrl());
                                       pictureObject.setPictureId(picture.getPictureId());
                                       pictureObject.setCaption(picture.getCaption());
                                       pictureObject.setCaptionTranslated(picture.getCaptionTranslated());
                                       picturesList.add(pictureObject);
                                   });
                                   agodaDetailedHotelsInfo.setPictures(picturesList);


                                   //set roomtypes list
                                   List<AgodaDetailedHotelInfoObject.RoomTypes> roomTypesList = new ArrayList<>();
                                   hotelFullInformation.getHotelFullFeed().getRoomtypes().getRoomtype().stream().forEach(roomType -> {
                                       AgodaDetailedHotelInfoObject.RoomTypes roomTypeObject = new AgodaDetailedHotelInfoObject.RoomTypes();
                                       roomTypeObject.setHotelId(roomType.getHotelId());
                                       roomTypeObject.setBedType(roomType.getBedType());
                                       roomTypeObject.setGender(roomType.getGender());
                                       roomTypeObject.setHotelRoomtypeId(roomType.getHotelRoomtypeId());
                                       roomTypeObject.setNoOfRoom(roomType.getNoOfRoom());
                                       roomTypeObject.setHotelMasterRoomtypeId(roomType.getHotelMasterRoomtypeId());
                                       roomTypeObject.setBedType(roomType.getBedType());
                                       roomTypeObject.setMaxInfantInRoom(roomType.getMaxInfantInRoom());
                                       roomTypeObject.setHotelRoomtypeAlternateName(roomType.getHotelRoomtypeAlternateName());
                                       roomTypeObject.setHotelRoomtypePicture(roomType.getHotelRoomtypePicture());
                                       roomTypeObject.setMaxInfantInRoom(roomType.getMaxInfantInRoom());
                                       roomTypeObject.setMaxExtrabeds(roomType.getMaxExtrabeds());
                                       roomTypeObject.setRoomSizeInclTerrace(roomType.getRoomSizeInclTerrace());
                                       roomTypeObject.setSharedBathroom(roomType.getSharedBathroom());
                                       roomTypeObject.setMaxOccupancyPerRoom(roomType.getMaxOccupancyPerRoom());
                                       roomTypeObject.setStandardCaption(roomType.getStandardCaption());
                                       roomTypeObject.setViews(roomType.getViews());

                                       roomTypesList.add(roomTypeObject);
                                   });
                                   agodaDetailedHotelsInfo.setRoomtypes(roomTypesList);

                                   //set value in HashMap
                                   if (agodaDetailedHotelsInfo.getHotels() != null && agodaDetailedHotelsInfo.getHotels().getHotelId() != null
                                           && agodaDetailedHotelsInfo.getHotels().getHotelName() != null) {
                                       detailedHotelInfoMap.put(agodaDetailedHotelsInfo.getHotels().getHotelId(), agodaDetailedHotelsInfo.getHotels().getHotelName());
                                   }
                                   synchronized (listToSave) {
                                       listToSave.add(agodaDetailedHotelsInfo);
                                   }
                               }
                           } catch (IOException e) {
                               throw new RuntimeException(e);
                           }

                       });
                   }
                   List<List<AgodaDetailedHotelInfoObject>> partListToSave = Lists.partition(listToSave, 100);
                   int count = 1;
                   for (List<AgodaDetailedHotelInfoObject> list : partListToSave) {
                       log.info("----------------Saving DetailedHotelInfoObject for partition number " + count + "----------------");
                       List<AgodaDetailedHotelInfoObject> savedList = agodaDetailedInfoForHotelRepository.saveAll(list);
                       log.info("Number of Hotels saved in list : " + savedList.size());
                       log.info("Detailed Hotel list saved for cityId : " + city.getCityId());
                       Optional<AgodaDetailedHotelInfoObject> startId = savedList.stream().filter(firstElement -> firstElement.getHotels() != null).findFirst();
                       if (startId.isPresent()) {
                           log.info("HotelId saved from : " + startId.get().getHotels().getHotelId());
                       }
                       Optional<AgodaDetailedHotelInfoObject> lastElement = savedList.stream().filter(list1 -> list1.getHotels() != null).reduce((first, second) -> second);
                       if (lastElement.isPresent()) {
                           log.info("HotelId saved unto : " + lastElement.get().getHotels().getHotelId());
                       }
//                   if (savedList != null && !savedList.isEmpty()) {
//                       agodaStaticDumpService.saveStaticDumpHotel(savedList,countriesCodeMap);
//                   }
                       log.info("----------------DetailedHotelInfo saved---------------");
                       count++;
                   }

               });

               long endTime = System.currentTimeMillis();
               long timeTaken = (endTime - startTime) / 1000;
               log.info("Time Taken in process :" + timeTaken + " sec");
               if (timeTaken >= 300) {
                   log.info("Time in minutes :" + (timeTaken / 60) + " min " + (timeTaken % 60) + " sec");
               }

               log.info("Data Saved for pageNumber : "+i);

           } else {
              log.info("No Hotels Found on Page Number : "+i);
           }
       }
        log.info("-----------------------------------------------------Application Execution Completed--------------------------------------------------");
       return new ResponseEntity("Data Saved",HttpStatus.OK);
    }

    //method to create HashMap for Detailed Hotel Info
    public Map<Integer,String> createDetailedHotelInfoMap(){

        Map<Integer,String> detailedHotelInfoMap = new HashMap<>();
        long start = System.currentTimeMillis();
        List<AgodaDetailedHotelInfoObject> projectedList = agodaDetailedInfoForHotelRepository.findAllProjected();
        long end = System.currentTimeMillis();
        log.info("Time to fetch : "+(end-start)/1000+" seconds");
            for (AgodaDetailedHotelInfoObject detailedHotelInfoObject : projectedList) {
                if (detailedHotelInfoObject.getHotels() != null && detailedHotelInfoObject.getHotels().getHotelId() != null) {
                    Integer key = detailedHotelInfoObject.getHotels().getHotelId();
                    String value = detailedHotelInfoObject.getHotels().getHotelName();
                    detailedHotelInfoMap.put(key, value);
                }
            }

        return detailedHotelInfoMap;
    }



    public HotelInformationByCity getHotelsListByCity(Integer cityId) throws IOException {

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://affiliatefeed.agoda.com/datafeeds/feed/getfeed";
        Integer feedId = 5;
        String apiKey = "EA32194E-CFD5-4786-A2D4-8315EE83D1DD";
        Integer mcityId = cityId;

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
                .queryParam("feed_id",feedId)
                .queryParam("apikey",apiKey)
                .queryParam("mcity_id",mcityId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(uriComponentsBuilder.toUriString(),HttpMethod.GET,entity,byte[].class);
        byte[] hotelFeedInfo = response.getBody();

        ByteArrayInputStream bis = new ByteArrayInputStream(hotelFeedInfo);
        GZIPInputStream gis = new GZIPInputStream(bis);

        InputStreamReader reader = new InputStreamReader(gis);
        ObjectMapper objectMapper = new ObjectMapper();
        HotelInformationByCity hotelInformationByCity = objectMapper.readValue(reader, HotelInformationByCity.class);

        return hotelInformationByCity;

    }

   public HotelFullInformation getFullHotelInformation(Integer mhotelId) throws IOException {

        RestTemplate restTemplate =new RestTemplate();
        String url = "http://affiliatefeed.agoda.com/datafeeds/feed/getfeed";
       String apiKey = "EA32194E-CFD5-4786-A2D4-8315EE83D1DD";
        Integer feedId = 19;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("feed_id",feedId)
                .queryParam("mhotel_id",mhotelId)
                .queryParam("apikey",apiKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,entity,byte[].class);
        byte[] fullHotelInfo = response.getBody();

        ByteArrayInputStream bis = new ByteArrayInputStream(fullHotelInfo);
        GZIPInputStream gis = new GZIPInputStream(bis);

        InputStreamReader reader = new InputStreamReader(gis);
        ObjectMapper objectMapper = new ObjectMapper();

        HotelFullInformation hotelFullInformation = objectMapper.readValue(reader,HotelFullInformation.class);

        return hotelFullInformation;
    }

    public PropertyAvailabilityRes getPropertyAvailability(PropertyAvailabilityReq propertyAvailabilityReq, OTADetail otaDetails) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String siteId = "1844203";
        String apiKey = "EA32194E-CFD5-4786-A2D4-8315EE83D1DD";
        RestTemplate restTemplate = new RestTemplate();
        String searchUrl = otaDetails.getSearchUrl();
        //String url = "http://sandbox-affiliateapi.agoda.com/api/v4/property/availability";

       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);

       String authorizationKey = siteId+":"+apiKey;
       headers.set("Authorization",authorizationKey);

       HttpEntity entity = new HttpEntity<>(propertyAvailabilityReq,headers);

       ResponseEntity<byte[]> response = restTemplate.exchange(searchUrl,HttpMethod.POST,entity,byte[].class);
       byte[] propertyRes = response.getBody();

       ByteArrayInputStream bis = new ByteArrayInputStream(propertyRes);

       InputStreamReader inputStreamReader = new InputStreamReader(bis);
       PropertyAvailabilityRes res = objectMapper.readValue(inputStreamReader,PropertyAvailabilityRes.class);

       return res;
    }

   public PreCheckRes preBookingCheckService(PreCheckReq req) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String siteId = "1844203";
        String apiKey = "EA32194E-CFD5-4786-A2D4-8315EE83D1DD";
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://affiliateapiservices.agoda.com/api/v2/prebooking/precheck";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String authorization = siteId+":"+apiKey;
        headers.set("Authorization",authorization);

        HttpEntity entity = new HttpEntity(req,headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(url,HttpMethod.POST,entity,byte[].class);
        byte[] preCheckresponse = response.getBody();

        ByteArrayInputStream bis = new ByteArrayInputStream(preCheckresponse);

        InputStreamReader reader = new InputStreamReader(bis);
        PreCheckRes preCheckRes = objectMapper.readValue(reader,PreCheckRes.class);

        return preCheckRes;

   }

   public BookPropertyRes bookService(BookPropertyReq bookPropertyReq) throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        String url = "https://sandbox-affiliateapisecure.agoda.com/api/v4/book";

        String siteId = "1844203";
        String apiKey = "EA32194E-CFD5-4786-A2D4-8315EE83D1DD";
        String authorization = siteId+":"+apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization",authorization);

        HttpEntity entity = new HttpEntity(bookPropertyReq,headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(url,HttpMethod.POST,entity,byte[].class);

        byte[] bookResponse = response.getBody();
        ByteArrayInputStream bis = new ByteArrayInputStream(bookResponse);
        InputStreamReader reader = new InputStreamReader(bis);

        BookPropertyRes res = mapper.readValue(reader, BookPropertyRes.class);
        return res;
   }

    public CancellationBookingResponseDTO getCancellationBookingResponse(CancellationRequestV4 cancellationRequestV4) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        String url = "https://sandbox-affiliateapisecure.agoda.com/api/v4/postBooking/cancel";
        String siteId = "1844203";
        String apiKey = "EA32194E-CFD5-4786-A2D4-8315EE83D1DD";
        String authorization = siteId+":"+apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization",authorization);

        HttpEntity entity = new HttpEntity<>(cancellationRequestV4,headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("apiKey",apiKey);

        ResponseEntity<byte[]> response = restTemplate.exchange(url,HttpMethod.POST,entity,byte[].class);

        byte[] cancellationResponse = response.getBody();
        ByteArrayInputStream bis = new ByteArrayInputStream(cancellationResponse);
        InputStreamReader reader = new InputStreamReader(bis);

        CancellationBookingResponseDTO cancellationBookingResponseDTO = mapper.readValue(reader,CancellationBookingResponseDTO.class);
        return cancellationBookingResponseDTO;

    }

   //Service for searching Agoda Hotels
   public SearchResponse agodaSearchHotels(OTARequest otaRequest) throws IOException {
        long startTime = System.currentTimeMillis();
        SearchRequest searchRequest = otaRequest.getSearchRequest();
        OTADetail otaDetails = otaRequest.getOtaDetail();

        long hotelSearchStart = System.currentTimeMillis();
        List<Integer> hotelIds = findHotelListByCityAndCountry(searchRequest.getCity(),searchRequest.getCountry());
        long hotelSearchEnd = System.currentTimeMillis();
        log.info("Time Taken to search all hotels within city :"+searchRequest.getCity()+" is "+(hotelSearchEnd-hotelSearchStart));

        if(hotelIds == null){
            return buildEmptySearchResponse();
        }
       PropertyAvailabilityRes finalPropertyAvailabilityRes = new PropertyAvailabilityRes();
        String cachedId = new String();
        //if fresh search i.e. cacheId is null
       if(searchRequest.getCacheId() == null || searchRequest.getCacheId().equals("")) {
           //we are creating cache in scrunch not here
         //   cachedId = saveCityHotelIdsCache(hotelIds, searchRequest.getCity());

           List<List<Integer>> partition = Lists.partition(hotelIds, 100);
          // PropertyAvailabilityRes finalPropertyAvailabilityRes = new PropertyAvailabilityRes();

           //Property Availability called again for room search so searchId will be used from there
           //finalPropertyAvailabilityRes.setSearchId(12L);
           finalPropertyAvailabilityRes.setProperties(new ArrayList<>());
           long searchStart = System.currentTimeMillis();
           for (List<Integer> partList : partition) {
               List<Integer> pids = pids = getPids(searchRequest);
               //Build propertyAvailabilityReq object for search request

               if(pids.size() == 0){
                   return buildEmptySearchResponse();
               }
               PropertyAvailabilityReq propertyAvailabilityReq = buildAgodaPropertySearchRequestForSearch(searchRequest, pids);
               logUtils.printDebugLogStatement(AgodaAPIConstants.Property_Search_Service, Thread.currentThread().getStackTrace()[1]
                       , "Request for property Availability Search", objectMapper.writeValueAsString(propertyAvailabilityReq));



               //Call propertyAvailabilityService to get property search response,we are sending ota details separately for agoda urls
               PropertyAvailabilityRes propertyAvailabilityRes = getPropertyAvailability(propertyAvailabilityReq, otaDetails);
               logUtils.printDebugLogStatement(AgodaAPIConstants.Property_Search_Service, Thread.currentThread().getStackTrace()[1]
                       , "Property Availability Search Response", objectMapper.writeValueAsString(propertyAvailabilityRes));



               try {
                   //finalPropertyAvailabilityRes.setProperties(propertyAvailabilityRes.getProperties());
                   finalPropertyAvailabilityRes.getProperties().addAll(propertyAvailabilityRes.getProperties());
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
           long searchEnd = System.currentTimeMillis();
           log.info("Agoda Property search time : "+(searchEnd-searchStart));
       }
       else{
           //need to send id instead of uuid
//           CityHotelIdsCache.PropertyIds propertyIds =  getHotelIdsByPageNumberAndId(searchRequest.getPageNumber(),searchRequest.getCacheId());
           finalPropertyAvailabilityRes = buildPropertyAvailabilityResponseForCachedHotelIds(otaRequest);
           cachedId = searchRequest.getCacheId();
       }

       //Build response for scrunch
       SearchResponse scrunchResponse = buildResponseForScrunch(finalPropertyAvailabilityRes,searchRequest,cachedId);
       scrunchResponse.setPropertyIds(hotelIds);
       logUtils.printDebugLogStatement(AgodaAPIConstants.Property_Search_Service,Thread.currentThread().getStackTrace()[1]
       ,"Property search Response for Scrunch",objectMapper.writeValueAsString(scrunchResponse));

       long timeTaken = System.currentTimeMillis()-startTime;
       log.info("Total time taken in search "+timeTaken);
       return scrunchResponse;
   }

   public PropertyAvailabilityRes buildPropertyAvailabilityResponseForCachedHotelIds(OTARequest otaRequest) throws IOException {
        List<Integer> pids = otaRequest.getSearchRequest().getPropertyIdsList();

       PropertyAvailabilityRes finalPropertyAvailabilityRes = new PropertyAvailabilityRes();
       SearchRequest searchRequest = otaRequest.getSearchRequest();
       OTADetail otaDetails = otaRequest.getOtaDetail();

       //Build propertyAvailabilityReq object for search request
       PropertyAvailabilityReq propertyAvailabilityReq = buildAgodaPropertySearchRequestForSearch(searchRequest,pids);
       logUtils.printDebugLogStatement(AgodaAPIConstants.Property_Search_Service,Thread.currentThread().getStackTrace()[1]
               ,"Request for property Availability Search",objectMapper.writeValueAsString(propertyAvailabilityReq));


       //Call propertyAvailabilityService to get property search response,we are sending ota details separately for agoda urls
       PropertyAvailabilityRes propertyAvailabilityRes = getPropertyAvailability(propertyAvailabilityReq,otaDetails);
       logUtils.printDebugLogStatement(AgodaAPIConstants.Property_Search_Service,Thread.currentThread().getStackTrace()[1]
               ,"Property Availability Search Response",objectMapper.writeValueAsString(propertyAvailabilityRes));

       try {
           //finalPropertyAvailabilityRes.setProperties(propertyAvailabilityRes.getProperties());
           finalPropertyAvailabilityRes.getProperties().addAll(propertyAvailabilityRes.getProperties());
       }catch(Exception e){
           e.printStackTrace();
       }

       return finalPropertyAvailabilityRes;
   }
    private static List<Integer> getPids(SearchRequest searchRequest) {
        List<Integer> pids = new ArrayList<>();
        if(searchRequest.getCity().equalsIgnoreCase("New Delhi and NCR")) {
             pids = Arrays.asList(4877);
        }
        else if (searchRequest.getCity().equalsIgnoreCase("Mumbai")) {
             pids = Arrays.asList(70697);
        }
        else if (searchRequest.getCity().equalsIgnoreCase("Hyderabad")) {
             pids = Arrays.asList(178188);
        }
        else if (searchRequest.getCity().equalsIgnoreCase("Chennai")) {
             pids = Arrays.asList(7947);
        }
        else if(searchRequest.getCity().equalsIgnoreCase("Las Vegas (NV)"))
        {
            pids = Arrays.asList(2937,147265);
        }
        else if (searchRequest.getCity().equalsIgnoreCase("New York (NY)")) {
           pids = Arrays.asList(105040,105262);
        }
        else if (searchRequest.getCity().equalsIgnoreCase("Singapore")) {
          pids = Arrays.asList(2256959);
        }
        else if (searchRequest.getCity().equalsIgnoreCase("Athens")){
            pids = Arrays.asList(4521);
        }
        else if (searchRequest.getCity().equalsIgnoreCase("Guangzhou"))
        {
            pids = Arrays.asList(9107);
        }
        else if (searchRequest.getCity().equalsIgnoreCase("Maldive Islands")) {
            pids = Arrays.asList(12153, 65498,12157);
        }
        return pids;
    }

//    public String saveCityHotelIdsCache(List<Integer> hotelIds,String city){
//        UUID uuid = UUID.randomUUID();
//        String id = generateIdForHotelIdsCache(uuid,city);
//        CityHotelIdsCache cityHotelIdsCache = new CityHotelIdsCache();
//        cityHotelIdsCache.setId(id);
//        Integer i = 1;
//        List<CityHotelIdsCache.PropertyIds> propertyIds = new ArrayList<>();
//        List<List<Integer>> partitionedList = Lists.partition(hotelIds,10);
//        for (List<Integer> hotelIdlist :  partitionedList){
//            CityHotelIdsCache.PropertyIds properties = new CityHotelIdsCache.PropertyIds();
//            properties.setPageNumber(i);
//            properties.setPropertyIdsList(hotelIdlist);
//            propertyIds.add(properties);
//            i++;
//        }
//        cityHotelIdsCache.setListOfpropertyIdsList(propertyIds);
//        cityHotelIdsCacheRepository.save(cityHotelIdsCache);
//
//        return id;
//    }

    public CityHotelIdsCache.PropertyIds getHotelIdsByPageNumberAndId(Integer pageNumber,String id){

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        query.fields().elemMatch("listOfpropertyIdsList", Criteria.where("pageNumber").is(pageNumber));

        CityHotelIdsCache result = mongoTemplate.findOne(query, CityHotelIdsCache.class);
        if(result != null)
        {
            return result.getListOfpropertyIdsList().get(0);
        }
        else
        {
            return new CityHotelIdsCache.PropertyIds();
        }
    }

    public String generateIdForHotelIdsCache(UUID uuid,String city){
        StringBuilder builder = new StringBuilder();
        builder.append(uuid.toString());
        builder.append("_");
        builder.append(city);
        String id = builder.toString();
        return id;
    }


    public List<Integer> findHotelListByCityAndCountry(String city, String country){
       HotelsListByCities hotelsListByCities = agodaHotelsListByCitiesV4Repository.findByCityNameIgnoreCaseAndCountryNameIgnoreCase(city,country);

       if(hotelsListByCities != null && hotelsListByCities.getHotelList() != null) {
           return hotelsListByCities.getHotelList();
       }
       else
           return null;
   }


   //Service to build request for property availability
   public PropertyAvailabilityReq buildAgodaPropertySearchRequestForSearch(SearchRequest searchRequest,List<Integer> hotelIds) throws JsonProcessingException {
        PropertyAvailabilityReq propertyAvailabilityReq =new PropertyAvailabilityReq();
        PropertyAvailabilityReq.Criteria criteria = new PropertyAvailabilityReq.Criteria();
        PropertyAvailabilityReq.Features features = new PropertyAvailabilityReq.Features();

        //set extra
        List<String> extra = Arrays.asList(
                "content",
                "surchargeDetail",
                "CancellationDetail",
                "BenefitDetail",
                "dailyRate",
                "taxDetail",
                "rateDetail",
                "promotionDetail"
        );

        //set criteria
        criteria.setPropertyIds(hotelIds);
        criteria.setCheckIn(searchRequest.getCheckInDate().toString());
        criteria.setCheckOut(searchRequest.getCheckOutDate().toString());
        criteria.setRooms(searchRequest.getNoOfRooms());
        searchRequest.getRooms().stream().forEach(roomBreakUp -> {

             criteria.setAdults(roomBreakUp.getNumOfAdults());
             criteria.setChildren((roomBreakUp.getNumOfChildren() == 0 || roomBreakUp.getNumOfChildren() == null) ? 0 : roomBreakUp.getNumOfChildren());
             //empty child list ages Array not allowed
             List<Integer> childrenAgesList = new ArrayList<>();
             if(roomBreakUp.getChildAges() != null) {
                 roomBreakUp.getChildAges().stream().forEach(integer -> {
                     childrenAgesList.add(integer);
                 });
                 criteria.setChildrenAges(childrenAgesList);
             }
             else
                 criteria.setChildrenAges(new ArrayList<>());

        });

        //currency in which result would be displayed
        criteria.setCurrency("MYR");
        criteria.setLanguage(AgodaConstants.LANGUAGE);
        criteria.setRatePlan(RatePlan.CUG.toString());
        criteria.setPlatform(Platform.Desktop.toString());

        //set country code in user country(we will get country name(nationality) from encompass, convert it into country code for agoda property search request)
       if(null != searchRequest.getNationality() && !searchRequest.getNationality().isEmpty() && !searchRequest.getNationality().equals("")) {
           criteria.setUserCountry(fetchUserCountryCode(searchRequest.getNationality()));
       }

        propertyAvailabilityReq.setCriteria(criteria);

        //set features
        features.setExtra(extra);
        features.setRatesPerProperty(25);
        propertyAvailabilityReq.setFeatures(features);
        propertyAvailabilityReq.setWaitTime(AgodaConstants.WAIT_TIME);

        return propertyAvailabilityReq;
   }


   //Service to build response for scrunch
   public SearchResponse buildResponseForScrunch(PropertyAvailabilityRes propertyAvailabilityRes,SearchRequest searchRequest,String cachedId){
       if(propertyAvailabilityRes.getProperties() != null) {
           LinkedHashMap<String, HotelDetails> result = new LinkedHashMap<>();

           propertyAvailabilityRes.getProperties().stream().forEach(properties -> {
               HotelDetails hotelDetails = new HotelDetails();
               hotelDetails.setHotelId(properties.getPropertyId().toString());

               //TreeMap to sort rooms so that room with the cheapest price can be determined as they haven't provided the cheapest room details in their response
               TreeMap<Double, PropertyAvailabilityRes.Properties.Rooms> roomsTreeMap = new TreeMap<>();
               properties.getRooms().stream().forEach(rooms -> {
                   roomsTreeMap.put(rooms.getTotalPayment().getInclusive(),rooms);
               });
               Double firstKey = roomsTreeMap.firstKey();
               //Get the cheapest room from tree map
               PropertyAvailabilityRes.Properties.Rooms cheapestRoom = roomsTreeMap.get(firstKey);

               hotelDetails.setBaseFare(BigDecimal.valueOf(cheapestRoom.getTotalPayment().getExclusive()));
               BigDecimal convertedAmount = BigDecimal.valueOf(cheapestRoom.getTotalPayment().getInclusive());
               Optional<String> currency = properties.getRooms().stream().map(rooms -> cheapestRoom.getRate().getCurrency()).findFirst();
               String currencyCode = currency.isPresent() ? currency.get() : null ;
               //Currency conversion logic to be built here
               hotelDetails.setLowestPrice(convertedAmount);
               hotelDetails.setRoomId(cheapestRoom.getRoomId().toString());
               hotelDetails.setTax(BigDecimal.valueOf(cheapestRoom.getTotalPayment().getTax()));
               hotelDetails.setOta(OTA.AGODA);
               hotelDetails.setRatePlanId(cheapestRoom.getRatePlanId());
               hotelDetails.setCurrencyCode(currencyCode);
               hotelDetails.setCacheId(cachedId);
               result.put(hotelDetails.getHotelId(),hotelDetails);

           });
           SearchResponse toReturn = new SearchResponse();
           toReturn.setHotelDetails(result);
           toReturn.setOta(OTA.AGODA);
           return toReturn;
       }
       else{
           return buildEmptySearchResponse();
       }

   }

   public SearchResponse buildEmptySearchResponse(){
           SearchResponse toReturn = new SearchResponse();
           toReturn.setHotelDetails(new LinkedHashMap<>());
           return toReturn;
       }

//   public PreCheckReq buildAgodaPreBookCheckRequest(OTARequest otaRequest){
//
//        BookingRequest bookingRequest = otaRequest.getBookingRequest();
//        SearchRequest searchRequest = otaRequest.getSearchRequest();
//
//        PreCheckReq preCheckReq = new PreCheckReq();
//        PreCheckReq.PrecheckDetails precheckDetails = new PreCheckReq.PrecheckDetails();
//
//        precheckDetails.setCheckIn(bookingRequest.getCheckInDate().toString());
//        precheckDetails.setCheckOut(bookingRequest.getCheckOutDate().toString());
//        precheckDetails.setSearchId(Long.valueOf(bookingRequest.getSearchId()));
//        //API will check booking duplication and return error without creating booking,When set to false
//        precheckDetails.setAllowDuplication(false);
//
//
//
//   }



    //Agoda Room Availability Search
    public RoomAvailabilityResponseV2 agodaRoomAvailabilitySearch(OTARequest otaRequest) throws IOException {

        RoomAvailabilityRequestV2 roomAvailabilityReqV2 = otaRequest.getRoomAvailabilityRequestV2();
        OTADetail otaDetail = otaRequest.getOtaDetail();
        //Fetch property id for which we need room info
        List<Integer> hotelId = Arrays.asList(Integer.valueOf(roomAvailabilityReqV2.getHotelId()));

        //Build agoda property and Room availability search request(We are basically again calling property availability url for rooms info for particular property)
        PropertyAvailabilityReq propertyAvailabilityReq = buildAgodaRoomAvailabilitySearchRequest(roomAvailabilityReqV2,hotelId);
        logUtils.printDebugLogStatement(AgodaAPIConstants.Room_Availability_Service,Thread.currentThread().getStackTrace()[1]
        ,"Room Availability Request",objectMapper.writeValueAsString(propertyAvailabilityReq));
        LOGGER.info("Request for room availability");

        //Now we will call Agoda and get the property availability response, which will also contain rooms info for that property/hotel
        PropertyAvailabilityRes propertyAvailabilityRes = getPropertyAvailability(propertyAvailabilityReq,otaDetail);
        logUtils.printDebugLogStatement(AgodaAPIConstants.Room_Availability_Service,Thread.currentThread().getStackTrace()[1]
        ,"Room Availability Response",objectMapper.writeValueAsString(propertyAvailabilityRes));

        //updateSessionIds for Hotel and rooms within it, they will be used when booking room
        updateSessionIds(propertyAvailabilityRes);

        //Build room availability response for scrunch
         RoomAvailabilityResponseV2 roomAvailabilityResponseV2 = buildResponseForScrunchRoomAvailability(propertyAvailabilityRes,roomAvailabilityReqV2);
         logUtils.printDebugLogStatement(AgodaAPIConstants.Room_Availability_Service,Thread.currentThread().getStackTrace()[1]
         ,"Room Availability Response for Scrunch",objectMapper.writeValueAsString(roomAvailabilityResponseV2));

         //Build session details and save
        roomAvailabilityService.buildRoomAvailabilitySessionDetailsAndSave(roomAvailabilityReqV2,roomAvailabilityResponseV2
                ,propertyAvailabilityReq,propertyAvailabilityRes);

         return roomAvailabilityResponseV2;
    }

    //Build Agoda Room Availability Request
    public PropertyAvailabilityReq buildAgodaRoomAvailabilitySearchRequest(RoomAvailabilityRequestV2 roomAvailabilityReqV2, List<Integer> hotelIds) throws JsonProcessingException {
       ObjectMapper objectMapper = new ObjectMapper();
        PropertyAvailabilityReq propertyAvailabilityReq =new PropertyAvailabilityReq();
        PropertyAvailabilityReq.Criteria criteria = new PropertyAvailabilityReq.Criteria();
        PropertyAvailabilityReq.Features features = new PropertyAvailabilityReq.Features();

        //set extra
        List<String> extra = Arrays.asList(
                "content",
                "surchargeDetail",
                "CancellationDetail",
                "BenefitDetail",
                "dailyRate",
                "taxDetail",
                "rateDetail",
                "promotionDetail"
        );

        //set criteria
        criteria.setPropertyIds(hotelIds);
        criteria.setCheckIn(roomAvailabilityReqV2.getCheckInDate().toString());
        criteria.setCheckOut(roomAvailabilityReqV2.getCheckOutDate().toString());
        criteria.setRooms(roomAvailabilityReqV2.getNoOfRooms());
        roomAvailabilityReqV2.getRooms().stream().forEach(roomBreakUp -> {
            criteria.setAdults(roomBreakUp.getNumOfAdults());
            criteria.setChildren(roomBreakUp.getNumOfChildren() != null ? roomBreakUp.getNumOfChildren() : 0);
            List<Integer> childrenAgesList = new ArrayList<>();
            if(roomBreakUp.getChildAges().size() != 0) {
                roomBreakUp.getChildAges().stream().forEach(integer -> {
                    childrenAgesList.add(integer);
                });
                criteria.setChildrenAges(childrenAgesList);
            }else
            {
                criteria.setChildrenAges(new ArrayList<>());
            }
        });

        //currency in which result would be displayed
        criteria.setCurrency(roomAvailabilityReqV2.getCurrency());
        criteria.setLanguage(AgodaConstants.LANGUAGE);
        criteria.setRatePlan(RatePlan.CUG.toString());
        criteria.setPlatform(Platform.Desktop.toString());

      // TODO: 11/08/23    //set UserCountryCode(for room availability scrunch is setting nationality to "IN" so may not need to call iso code)
        if (roomAvailabilityReqV2.getNationality() != null) {
            criteria.setUserCountry(roomAvailabilityReqV2.getNationality());
        }

        propertyAvailabilityReq.setCriteria(criteria);
        //set features
        features.setExtra(extra);
        features.setRatesPerProperty(AgodaConstants.RATES_PER_PROPERTY);
        propertyAvailabilityReq.setFeatures(features);
        propertyAvailabilityReq.setWaitTime(AgodaConstants.WAIT_TIME);

        return propertyAvailabilityReq;
    }

    public String fetchUserCountryCode(String nationality){
        String countryCode = agodaCountryV4Repository.findCountryIso2ByCountryNameIgnoreCase(nationality);
        //fetching countrycode as we are getting json object from MongoDB
        String jsonString = countryCode;
        JSONObject object = new JSONObject(jsonString);
        String countryCodeValue = object.getString("countryIso2");
        return countryCodeValue;
    }

    //Build Agoda Response For Room Availability
    public RoomAvailabilityResponseV2 buildResponseForScrunchRoomAvailability(PropertyAvailabilityRes propertyAvailabilityRes,
                                                                              RoomAvailabilityRequestV2 roomAvailabilityRequestV2){

        String id = buildIdForStaticDumpObject(propertyAvailabilityRes.getProperties().get(0).getPropertyId().toString());
        StaticDumpHotel staticDumpObject = agodaStaticDumpService.fetchStaticDumpHotel(id);
        String hotelRemarks = staticDumpObject.getInformation().getHotelRemarks();
        //get the property details as we are sending one property in search so use index 0
        PropertyAvailabilityRes.Properties properties = propertyAvailabilityRes.getProperties().get(0);

        //build room availability response for scrunch
        RoomAvailabilityResponseV2 toReturn = RoomAvailabilityResponseV2.builder().hotelSessionId(properties.getPropertySessionId())
                .hotelId(properties.getPropertyId().toString())
                .ota(OTA.AGODA)
                .checkInDate(roomAvailabilityRequestV2.getCheckInDate())
                .checkOutDate(roomAvailabilityRequestV2.getCheckOutDate())
                .rooms(buildRoomForRoomAvailability(roomAvailabilityRequestV2,properties))
                .hotelRemarks(hotelRemarks)//hotel remarks from static feed
                .build();

        return toReturn;

    }

    public static String buildIdForStaticDumpObject(String id){
        if(id != null && !id.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append(id);
            builder.append("_");
            builder.append(OTA.AGODA);
            String idToSet = builder.toString();
            return idToSet;
        }
        else
            return "";
    }

    public List<RoomAvailabilityResponseV2.Rooms> buildRoomForRoomAvailability(RoomAvailabilityRequestV2 roomAvailabilityRequestV2,
                                                                               PropertyAvailabilityRes.Properties properties){


         return properties.getRooms().stream().map(rooms -> {
            RoomAvailabilityResponseV2.Rooms rooms1 = RoomAvailabilityResponseV2.Rooms.builder()
                    .applicableAddons(null)
                    .availableQuantity(null)
                    .bedTypes(null)
                    .availableQuantity(Long.valueOf(rooms.getRemainingRooms()))
                    .cancellationExpiryDate(extractPolicyDate(rooms.getCancellationPolicy()))
                    .cancellationPolicyDescription(Arrays.asList(rooms.getCancellationPolicy().getCancellationText()))
                    .cancellationPolicyDetails(null)
                    .dateWisePrice(null)
                    .extraBeds(rooms.getExtraBeds())
                    .deal(buildDealForRoomAvailability(rooms.getPromotionDetail()))
                    .inclusions(buildInclusionsForRoomAvailability(rooms.getPromotionDetail(),rooms.getBenefits()))
                    .mealPlan(null)
                    .price(buildPriceForRoomAvailability(rooms))
                    .rateCommentsId(null)
                    .roomId(rooms.getRoomId().toString())
                    .roomSessionId(rooms.getRoomSessionId())
                    .smokingPreference(null)
                    .surcharges(buildSurchargesForRoomAvailability(rooms.getSurcharges()))
                    .build();
            return rooms1;
         }).collect(Collectors.toList());

    }

    public LocalDate extractPolicyDate(PropertyAvailabilityRes.Properties.Rooms.CancellationPolicy cancellationPolicy){
        AtomicReference<LocalDate> toReturn = new AtomicReference<>(null);
        cancellationPolicy.getDate().stream().filter(date ->  date.getRate().getInclusive().equals(Double.valueOf(0))).findFirst().ifPresent(date -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(date.getBefore(), formatter);
            LocalDate date1 = dateTime.toLocalDate();
            toReturn.set(date1);
        });
        return toReturn.get();

    }

    public RoomAvailabilityResponseV2.Deal buildDealForRoomAvailability(PropertyAvailabilityRes.Properties.Rooms.PromotionDetail promotionDetail){

        if(promotionDetail != null){
            return RoomAvailabilityResponseV2.Deal.builder()
                    .bookingScope("Per-Booking")
                    .description(promotionDetail.getDescription())
                    .discountAmount(BigDecimal.valueOf(promotionDetail.getSavingAmount()))
                    .title(promotionDetail.getSupplierPromoTypeDescription())
                    .type(null)
                    .value(null)
                    .build();
        }
        else {
            return null;
        }
    }


    public List<String> buildInclusionsForRoomAvailability(PropertyAvailabilityRes.Properties.Rooms.PromotionDetail promotionDetail
            ,List<PropertyAvailabilityRes.Properties.Rooms.Benefits> benefits) {

        List<String> toReturn = new ArrayList<>();
        if (benefits != null) {
            List<String> addBenefits = benefits.stream().map(benefits1 -> benefits1.getBenefitName()).collect(Collectors.toList());
            toReturn.addAll(addBenefits);
        }

        return toReturn;
    }

    public RoomAvailabilityResponseV2.Price buildPriceForRoomAvailability(PropertyAvailabilityRes.Properties.Rooms rooms)
    {
        return RoomAvailabilityResponseV2.Price.builder()
                .addOnAmount(null)
                .baseAmount(BigDecimal.valueOf(rooms.getTotalPayment().getExclusive()))
                .currency(rooms.getRate().getCurrency())
                .taxAmount(BigDecimal.valueOf(rooms.getTotalPayment().getTax()))
                .totalAmount(BigDecimal.valueOf(rooms.getTotalPayment().getInclusive()))
                .discountAmount(rooms.getPromotionDetail() !=null ? BigDecimal.valueOf(rooms.getPromotionDetail().getSavingAmount()) : null)
                .build();
    }

    public List<RoomAvailabilityResponseV2.Surcharges> buildSurchargesForRoomAvailability(List<PropertyAvailabilityRes.Properties.Rooms.Surcharges> surcharges)
    {
        if(surcharges == null || surcharges.isEmpty()){
            return null;
        }
        else {
            return surcharges.stream().map(surcharges1 -> {
                return RoomAvailabilityResponseV2.Surcharges.builder()
                        .exclusive(BigDecimal.valueOf(surcharges1.getRate().getExclusive()))
                        .inclusive(BigDecimal.valueOf(surcharges1.getRate().getInclusive()))
                        .tax(BigDecimal.valueOf(surcharges1.getRate().getTax()))
                        .name(surcharges1.getName())
                        .payAtHotel(surcharges1.getCharge().equalsIgnoreCase("Excluded") ? true : false)
                        .build();
            }).collect(Collectors.toList());
        }
    }


    public void updateSessionIds(PropertyAvailabilityRes propertyAvailabilityRes){
         propertyAvailabilityRes.getProperties().stream().forEach(properties -> properties.getRooms().stream()
                 .forEach(rooms -> rooms.setRoomSessionId(RandomUtils.randomAlphaNumeric(10))));

         propertyAvailabilityRes.getProperties().get(0).setPropertySessionId(ObjectId.get().toString());
    }



}



