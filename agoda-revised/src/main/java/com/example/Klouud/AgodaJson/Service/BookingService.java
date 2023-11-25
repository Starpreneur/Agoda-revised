package com.example.Klouud.AgodaJson.Service;

import com.example.Klouud.AgodaJson.Enitity.BookingV2;
import com.example.Klouud.AgodaJson.Enitity.RoomAvailabilityArchives;
import com.example.Klouud.AgodaJson.Enitity.RoomAvailabilitySessionDetails;
import com.example.Klouud.AgodaJson.Enum.OTA;
import com.example.Klouud.AgodaJson.Enum.PaymentModel;
import com.example.Klouud.AgodaJson.Enum.SOURCE;
import com.example.Klouud.AgodaJson.Model.*;
import com.example.Klouud.AgodaJson.Model.OtaDTOs.OTARequest;
import com.example.Klouud.AgodaJson.Repository.BookingV2Repository;
import com.example.Klouud.AgodaJson.Repository.CancellationBookingDetailRepository;
import com.example.Klouud.AgodaJson.Repository.RoomAvailabilityArchivesRepository;
import com.example.Klouud.AgodaJson.Repository.RoomAvailabilitySessionDetailsRepository;
import com.example.Klouud.AgodaJson.Utils.AgodaAPIConstants;
import com.example.Klouud.AgodaJson.Utils.LogUtils;
import com.example.Klouud.AgodaJson.Utils.StaticDataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
public class BookingService {



    @Autowired
    private AgodaFeedService agodaFeedService;

    @Autowired
    private RoomAvailabilitySessionDetailsRepository roomAvailabilitySessionDetailsRepository;

    @Autowired
    private LogUtils logUtils;

    @Autowired
    private StaticDataUtils staticDataUtils;

    @Autowired
    private RoomAvailabilityArchivesRepository roomAvailabilityArchivesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingV2Repository bookingV2Repository;

    @Autowired
    private CancellationBookingDetailRepository cancellationBookingDetailRepository;



    //Service for PreBooking Check
    public PreCheckRes preBookingCheck(OTARequest otaRequest,PropertyAvailabilityReq propertyAvailabilityReq
                                       ,PropertyAvailabilityRes propertyAvailabilityRes,RoomAvailabilityRequestV2 roomAvailabilityRequestV2
                                       ,RoomAvailabilityResponseV2 roomAvailabilityResponseV2) throws IOException {
        BookingRequestV2 bookingRequestV2 = otaRequest.getBookingRequestV2();

        //Build preBooking check request object
        PreCheckReq preCheckReq = buildRequestForPreBookingCheck(otaRequest,propertyAvailabilityReq,propertyAvailabilityRes);
        logUtils.printDebugLogStatement(AgodaAPIConstants.Book_Room_Service,Thread.currentThread().getStackTrace()[1]
                                                 ,"PreBookingCheck Request",objectMapper.writeValueAsString(preCheckReq));

        //Call preBooking Check service
        PreCheckRes preCheckRes = agodaFeedService.preBookingCheckService(preCheckReq);
        logUtils.printDebugLogStatement(AgodaAPIConstants.Book_Room_Service,Thread.currentThread().getStackTrace()[1]
                                            ,"PreBookingCheck response",objectMapper.writeValueAsString(preCheckRes));

        return preCheckRes;

    }

    //Service for booking room
    public BookingResponseV2 agodaBookingService(OTARequest otaRequest) throws Exception {

        BookingRequestV2 bookingRequestV2 = otaRequest.getBookingRequestV2();
        //fetch roomAvailability session details
        RoomAvailabilitySessionDetails roomAvailabilitySessionDetails = fetchRoomAvailabilitySessionDetailsWithSelectedRoomSession(bookingRequestV2.getHotelSessionId()
                                                                                  ,bookingRequestV2.getRoomSessionId());

        //Fetch property availability request/response from room session details
        PropertyAvailabilityReq propertyAvailabilityReq = (PropertyAvailabilityReq) roomAvailabilitySessionDetails.getOtaRequest();
        PropertyAvailabilityRes propertyAvailabilityRes = (PropertyAvailabilityRes) roomAvailabilitySessionDetails.getOtaResponse();

        //Fetch RoomAvailability request/response from room session details
        RoomAvailabilityRequestV2 roomAvailabilityRequestV2 = (RoomAvailabilityRequestV2) roomAvailabilitySessionDetails.getClientRequest();
        RoomAvailabilityResponseV2 roomAvailabilityResponseV2 = (RoomAvailabilityResponseV2) roomAvailabilitySessionDetails.getClientResponse();

        //call prebooking check
        PreCheckRes preCheckRes = preBookingCheck(otaRequest,propertyAvailabilityReq,propertyAvailabilityRes,roomAvailabilityRequestV2,roomAvailabilityResponseV2);
        log.info("Pre Booking Check Response : "+preCheckRes);

        if(preCheckRes.getStatus().equals("200")) {
            //build request for Agoda Booking service
            BookPropertyReq bookPropertyReq = buildRequestForAgodaBookingService(otaRequest, propertyAvailabilityReq, propertyAvailabilityRes);
            logUtils.printDebugLogStatement(AgodaAPIConstants.Book_Room_Service, Thread.currentThread().getStackTrace()[1]
                    ,"Book Room Request",objectMapper.writeValueAsString(bookPropertyReq) );

            //call Agoda booking service
           BookPropertyRes  bookPropertyRes = agodaFeedService.bookService(bookPropertyReq);
           logUtils.printDebugLogStatement(AgodaAPIConstants.Book_Room_Service,Thread.currentThread().getStackTrace()[1]
                   ,"Book Room Response",objectMapper.writeValueAsString(bookPropertyRes) );

            //Convert booking response to scrunch response
            BookingResponseV2 bookingResponseV2 = buildBookingResponseForScrunch(bookPropertyReq.getBookingDetails().getTag(), bookPropertyRes.getBookingDetails().get(0).getId());
            logUtils.printDebugLogStatement(AgodaAPIConstants.Book_Room_Service,Thread.currentThread().getStackTrace()[1]
                    ,"Booking Response for Scrunch",objectMapper.writeValueAsString(bookingResponseV2));

            //Saving booking
            saveBookingDetails(otaRequest.getOtaDetail().getOta(), bookingResponseV2.getBookingReferenceId(), bookingRequestV2,
                    bookingResponseV2, bookPropertyReq, bookPropertyRes, roomAvailabilitySessionDetails, otaRequest.getOtaDetail().getSource());
            log.info("Booking Details Saved");

            return bookingResponseV2;
        }
        else
            throw  new Exception(preCheckRes.getErrorList().get(0).getMessage());
    }

    private void saveBookingDetails(OTA ota, String bookingReferenceId,
                                    BookingRequestV2 clientRequest, BookingResponseV2 clientResponse,
                                    Object otaRequest, Object otaResponse, RoomAvailabilitySessionDetails roomAvailabilitySessionDetails, SOURCE source) {
        BookingV2 bookingV2 = new BookingV2();
        bookingV2.setId(bookingReferenceId);
        bookingV2.setOta(ota);
        bookingV2.setTag(clientRequest.getTag());
        bookingV2.setRoomAvailabilityArchivesId(clientRequest.getHotelSessionId());
        bookingV2.setTransactionId(clientRequest.getTransactionId());

        BookingV2.CreateBookingDetails bookingDetails = new BookingV2.CreateBookingDetails();
        bookingDetails.setClientRequest(clientRequest);
        bookingDetails.setClientResponse(clientResponse);
        bookingDetails.setOtaRequest(otaRequest);
        bookingDetails.setOtaResponse(otaResponse);
        bookingV2.setCreateBooking(bookingDetails);

        bookingV2.setBookingStatus("CREATED");
        bookingV2.setCreatedDateTime(LocalDateTime.now());
        bookingV2.setLoggedInUser(clientRequest.getLoggedInUser());
        bookingV2.setSource(source.name());
        BookingV2 save = bookingV2Repository.save(bookingV2);

        RoomAvailabilityArchives roomAvailabilityArchives = new RoomAvailabilityArchives();
        BeanUtils.copyProperties(roomAvailabilitySessionDetails, roomAvailabilityArchives);
        roomAvailabilityArchivesRepository.save(roomAvailabilityArchives);

    }


    //Service to fetch Room Session Details
    public RoomAvailabilitySessionDetails fetchRoomAvailabilitySessionDetailsWithSelectedRoomSession(String hotelSessionId,String roomSessionId){
      Optional <RoomAvailabilitySessionDetails> roomAvailabilitySessionDetailsOptional = roomAvailabilitySessionDetailsRepository.findById(hotelSessionId);
      RoomAvailabilitySessionDetails sessionDetails = new RoomAvailabilitySessionDetails();
      if(roomAvailabilitySessionDetailsOptional.isPresent()){
          sessionDetails = roomAvailabilitySessionDetailsOptional.get();
      }
        //get propertyAvailabilityRes(otaResponse) from sessionDetails
        PropertyAvailabilityRes propertyAvailabilityRes = (PropertyAvailabilityRes) sessionDetails.getOtaResponse();

        //fetch room with roomSessionId provided
        PropertyAvailabilityRes.Properties.Rooms room = propertyAvailabilityRes.getProperties().get(0).getRooms().stream()
                .filter(rooms -> roomSessionId.equals(roomSessionId)).findFirst().get();

        //set fetched room in propertyAvailabilityRes (now only this room will be in response not others as roomSessionId for this is given for booking)
        propertyAvailabilityRes.getProperties().get(0).setRooms(Arrays.asList(room));

        sessionDetails.setOtaResponse(propertyAvailabilityRes);

        return sessionDetails;
    }

    //Build Request Object For PreBooking Check
    public PreCheckReq buildRequestForPreBookingCheck(OTARequest otaRequest,PropertyAvailabilityReq propertyAvailabilityReq
                                                            ,PropertyAvailabilityRes propertyAvailabilityRes){
        PreCheckReq preCheckReq = new PreCheckReq();
        PreCheckReq.PrecheckDetails precheckDetails = new PreCheckReq.PrecheckDetails();
        precheckDetails.setCheckIn(propertyAvailabilityReq.getCriteria().getCheckIn());
        precheckDetails.setCheckOut(propertyAvailabilityReq.getCriteria().getCheckOut());
        precheckDetails.setTag(otaRequest.getBookingRequestV2().getTag());
        precheckDetails.setLanguage(AgodaConstants.LANGUAGE);
        precheckDetails.setAllowDuplication(false);
        precheckDetails.setProperty(setPropertyDetailsForPreBookingCheck(otaRequest,propertyAvailabilityReq,propertyAvailabilityRes));
        precheckDetails.setUserCountry(propertyAvailabilityReq.getCriteria().getUserCountry());
        precheckDetails.setSearchId(propertyAvailabilityRes.getSearchId());
        precheckDetails.setPlatform("Desktop");
        precheckDetails.setRatePlan("CUG");
        preCheckReq.setPrecheckDetails(precheckDetails);

        return preCheckReq;
    }

    //Build Request for agoda booking Service
    public BookPropertyReq buildRequestForAgodaBookingService(OTARequest otaRequest,PropertyAvailabilityReq propertyAvailabilityReq
                                                           ,PropertyAvailabilityRes propertyAvailabilityRes){

        BookPropertyReq bookPropertyReq = new BookPropertyReq();
        bookPropertyReq.setWaitTime(AgodaConstants.WAIT_TIME);


        //set Booking Details
        bookPropertyReq.setBookingDetails(setBookingDetails(otaRequest,propertyAvailabilityReq,propertyAvailabilityRes));

        //set Customer Details
        bookPropertyReq.setCustomerDetail(setCustomerDetails(otaRequest,propertyAvailabilityReq,propertyAvailabilityRes));

        //set Payment Details
        bookPropertyReq.setPaymentDetails(setDummyPaymentDetails(otaRequest));

        return bookPropertyReq;
    }


    //Build Booking Details
    public BookPropertyReq.BookingDetails setBookingDetails(OTARequest otaRequest,PropertyAvailabilityReq propertyAvailabilityReq
                                                                 ,PropertyAvailabilityRes propertyAvailabilityRes){

        BookPropertyReq.BookingDetails bookingDetails = new BookPropertyReq.BookingDetails();
        bookingDetails.setLanguage(AgodaConstants.LANGUAGE);
        bookingDetails.setCheckIn(propertyAvailabilityReq.getCriteria().getCheckIn());
        bookingDetails.setCheckOut(propertyAvailabilityReq.getCriteria().getCheckOut());
        bookingDetails.setSearchId(propertyAvailabilityRes.getSearchId());
        bookingDetails.setUserCountry(propertyAvailabilityReq.getCriteria().getUserCountry());
        bookingDetails.setAllowDuplication(false);
        bookingDetails.setTag(otaRequest.getBookingRequestV2().getTag());

        //set property details within booking details
        bookingDetails.setProperty(setPropertyWithInBookingDetails(otaRequest,
                propertyAvailabilityReq,propertyAvailabilityRes));

        return bookingDetails;
    }

    //Set Property Details For Pre Booking Check
    public PreCheckReq.PrecheckDetails.Property setPropertyDetailsForPreBookingCheck(OTARequest otaRequest,PropertyAvailabilityReq propertyAvailabilityReq
                                                                                             , PropertyAvailabilityRes propertyAvailabilityRes){
        PreCheckReq.PrecheckDetails.Property property = new PreCheckReq.PrecheckDetails.Property();
        property.setPropertyId(propertyAvailabilityReq.getCriteria().getPropertyIds().get(0));

        //Set rooms within property
        PreCheckReq.PrecheckDetails.Property.Rooms rooms = new PreCheckReq.PrecheckDetails.Property.Rooms();
        rooms.setAdults(propertyAvailabilityReq.getCriteria().getAdults());
        rooms.setChildren(propertyAvailabilityReq.getCriteria().getChildren());
        rooms.setCurrency(propertyAvailabilityReq.getCriteria().getCurrency());
        rooms.setCount(propertyAvailabilityReq.getCriteria().getRooms());
        rooms.setBlockId(propertyAvailabilityRes.getProperties().get(0).getRooms().get(0).getBlockId());
        rooms.setChildrenAges(propertyAvailabilityReq.getCriteria().getChildrenAges());
        rooms.setPaymentModel(PaymentModel.Merchant.toString());

        //Set rates within room
        PreCheckReq.PrecheckDetails.Property.Rooms.Rate rate = new PreCheckReq.PrecheckDetails.Property.Rooms.Rate();
        rate.setInclusive(propertyAvailabilityRes.getProperties().get(0).getRooms().get(0).getRate().getInclusive());

        rooms.setRate(rate);

        //Set surcharges within rate
       List< PreCheckReq.PrecheckDetails.Property.Rooms.Surcharges > surcharges = new ArrayList<>();
        propertyAvailabilityRes.getProperties().get(0).getRooms().get(0).getSurcharges().stream().forEach(surcharges1 -> {
            PreCheckReq.PrecheckDetails.Property.Rooms.Surcharges toAdd = new PreCheckReq.PrecheckDetails.Property.Rooms.Surcharges();
            toAdd.setId(Long.valueOf(surcharges1.getId()));
            PreCheckReq.PrecheckDetails.Property.Rooms.Surcharges.Rate rate1 = new PreCheckReq.PrecheckDetails.Property.Rooms.Surcharges.Rate();
            rate1.setInclusive(surcharges1.getRate().getInclusive());
            toAdd.setRate(rate1);

            surcharges.add(toAdd);
        });

        rooms.setSurcharges(surcharges);
        property.setRooms(Arrays.asList(rooms));

        return property;
    }

    public BookPropertyReq.BookingDetails.Property setPropertyWithInBookingDetails(OTARequest otaRequest,PropertyAvailabilityReq propertyAvailabilityReq
                                                                                     ,PropertyAvailabilityRes propertyAvailabilityRes){
        BookPropertyReq.BookingDetails.Property property = new BookPropertyReq.BookingDetails.Property();
        property.setPropertyId(propertyAvailabilityReq.getCriteria().getPropertyIds().get(0));

        //set room within property
        BookPropertyReq.BookingDetails.Property.Rooms rooms = new BookPropertyReq.BookingDetails.Property.Rooms();
        rooms.setAdults(propertyAvailabilityReq.getCriteria().getAdults());
        rooms.setChildren(propertyAvailabilityReq.getCriteria().getChildren());
        rooms.setCurrency(propertyAvailabilityReq.getCriteria().getCurrency() != null ? propertyAvailabilityReq.getCriteria().getCurrency() : AgodaConstants.DEFAULT_CURRENCY);
        rooms.setBlockId(propertyAvailabilityRes.getProperties().get(0).getRooms().get(0).getBlockId());
        rooms.setChildrenAges(propertyAvailabilityReq.getCriteria().getChildrenAges().size() != 0 ? propertyAvailabilityReq.getCriteria().getChildrenAges() : null);

        //rooms.setBookNowPayLaterDate("2023-09-09");
        rooms.setPaymentModel(PaymentModel.Merchant.toString());
        rooms.setSpecialRequest("no special request");

        BookPropertyReq.BookingDetails.Property.Rooms.Rate rate = new BookPropertyReq.BookingDetails.Property.Rooms.Rate();
        rate.setInclusive(propertyAvailabilityRes.getProperties().get(0).getRooms().get(0).getRate().getInclusive());

        rooms.setRate(rate);

        //set surcharges within room
        List<BookPropertyReq.BookingDetails.Property.Rooms.Surcharges> surcharges = new ArrayList<>();
        propertyAvailabilityRes.getProperties().get(0).getRooms().get(0).getSurcharges().stream().forEach(surcharges1 -> {
            BookPropertyReq.BookingDetails.Property.Rooms.Surcharges toAdd = new BookPropertyReq.BookingDetails.Property.Rooms.Surcharges();
            toAdd.setId(Long.valueOf(surcharges1.getId()));
            BookPropertyReq.BookingDetails.Property.Rooms.Surcharges.Rate rateToAdd = new BookPropertyReq.BookingDetails.Property.Rooms.Surcharges.Rate();
            rateToAdd.setInclusive(surcharges1.getRate().getInclusive());
            rateToAdd.setFees(surcharges1.getRate().getFees());
            rateToAdd.setTax(surcharges1.getRate().getTax());
            rateToAdd.setExclusive(surcharges1.getRate().getExclusive());
            rateToAdd.setMargin(surcharges1.getMargin());
            toAdd.setRate(rateToAdd);
            surcharges.add(toAdd);
        });

        rooms.setSurcharges(surcharges);

        //set guestDetails within rooms
        BookPropertyReq.BookingDetails.Property.Rooms.GuestDetails guestDetails = new BookPropertyReq.BookingDetails.Property.Rooms.GuestDetails();
        guestDetails.setAge(otaRequest.getBookingRequestV2().getPrimaryGuestDetails().getAge() !=  null
                ? Math.toIntExact(otaRequest.getBookingRequestV2().getPrimaryGuestDetails().getAge()) : AgodaConstants.DEFAULT_AGE);
        guestDetails.setFirstName(otaRequest.getBookingRequestV2().getPrimaryGuestDetails().getFirstName());
        guestDetails.setLastName(otaRequest.getBookingRequestV2().getPrimaryGuestDetails().getLastName());
        guestDetails.setCountryOfResidence(getCountryOfResidence(otaRequest.getBookingRequestV2().getPrimaryGuestDetails().getCountry()));
        guestDetails.setPrimary(Boolean.TRUE);
        guestDetails.setTitle("Mr.");
        guestDetails.setIsChild(false);
        guestDetails.setGender("Male");

        rooms.setGuestDetails(Arrays.asList(guestDetails));

        //set count of rooms(number of rooms required)
        rooms.setCount(propertyAvailabilityReq.getCriteria().getRooms());

        property.setRooms(Arrays.asList(rooms));
        return property;
    }

    public String getCountryOfResidence(String countryFromBookingRequest){
        String regex = "^[a-zA-Z]{2}$";
        Pattern pattern = Pattern.compile(regex);

        boolean result =  pattern.matcher(countryFromBookingRequest).matches();
        if(result){
            return countryFromBookingRequest;
        }
        else{
            return staticDataUtils.countryCode(countryFromBookingRequest);
        }

    }

    public BookPropertyReq.CustomerDetail setCustomerDetails(OTARequest otaRequest,PropertyAvailabilityReq propertyAvailabilityReq
                                                                 ,PropertyAvailabilityRes propertyAvailabilityRes){
        BookPropertyReq.CustomerDetail customerDetail = new BookPropertyReq.CustomerDetail();
        customerDetail.setLanguage(AgodaConstants.LANGUAGE);
        customerDetail.setFirstName(otaRequest.getBookingRequestV2().getPrimaryGuestDetails().getFirstName());
        customerDetail.setLastName(otaRequest.getBookingRequestV2().getPrimaryGuestDetails().getLastName());
        customerDetail.setNewsletter(false);
        //title cannot be null
        customerDetail.setTitle("Mr.");
        customerDetail.setEmail(otaRequest.getBookingRequestV2().getPrimaryGuestDetails().getEmailId());
        //set phone
        BookPropertyReq.CustomerDetail.Phone phone = new BookPropertyReq.CustomerDetail.Phone();
        phone.setNumber(otaRequest.getBookingRequestV2().getPrimaryGuestDetails().getMobileNumber());
        //country code and area code should not be null(need them in booking request)
        phone.setCountryCode("91");
        phone.setAreaCode("2");
        customerDetail.setPhone(phone);

        return customerDetail;

    }


    //Set Payment Details
    public BookPropertyReq.PaymentDetails setDummyPaymentDetails(OTARequest otaRequest){
        SOURCE source = otaRequest.getOtaDetail().getSource();
        BookPropertyReq.PaymentDetails paymentDetails = new BookPropertyReq.PaymentDetails();
        //set credit card info with in payment details
        BookPropertyReq.PaymentDetails.CreditCardInfo creditCardInfo = new BookPropertyReq.PaymentDetails.CreditCardInfo();
        creditCardInfo.setCardType("MasterCard");
        //credit card number must be 15 or 16 digit
        creditCardInfo.setNumber("938388339399399");
        creditCardInfo.setCvc("345");
        creditCardInfo.setHolderName("John doe");
        //expiry date should be "MMYYYY" format
        creditCardInfo.setExpiryDate("082025");
        creditCardInfo.setCountryOfIssue("IN");
        creditCardInfo.setIssuingBank("Bank");

        paymentDetails.setCreditCardInfo(creditCardInfo);

        return paymentDetails;
    }


    public BookingResponseV2 buildBookingResponseForScrunch(String tag,String otaReferenceId){

        BookingResponseV2 bookingResponseV2 = new BookingResponseV2();
        bookingResponseV2.setTag(tag);
        bookingResponseV2.setOtaBookingReferenceId(otaReferenceId);
        bookingResponseV2.setBookingReferenceId(ObjectId.get().toString());
        bookingResponseV2.setBookingStatus("Created");

        return bookingResponseV2;

    }


    public CancellationBookingResponseDTO cancelBookingService(CancellationRequestV4 cancellationRequestV4) throws IOException {
        log.info("Cancel Booking Request : "+cancellationRequestV4);
      CancellationBookingResponseDTO cancellationBookingResponseDTO = agodaFeedService.getCancellationBookingResponse(cancellationRequestV4);
      log.info("cancel Booking Response : "+cancellationBookingResponseDTO);
      ObjectMapper mapper = new ObjectMapper();
      CancellationBookingResponseDetailsFromAgoda cancellationBookingResponseDetailsFromAgoda = mapper.convertValue(cancellationBookingResponseDTO
                                                                                                       ,CancellationBookingResponseDetailsFromAgoda.class);
      CancellationBookingResponseDetailsFromAgoda savedCancellationBookingDetail = cancellationBookingDetailRepository.save(cancellationBookingResponseDetailsFromAgoda);
      log.info("Booking Cancelled for bookingId : "+cancellationRequestV4.getBookingId());
      return cancellationBookingResponseDTO;

    }
}
