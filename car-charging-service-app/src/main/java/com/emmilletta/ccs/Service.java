package com.emmilletta.ccs;

import com.emmilletta.ccs.client.asia.AsiaClient;
import com.emmilletta.ccs.client.asia.AsiaErrorCode;
import com.emmilletta.ccs.client.asia.dto.AsiaBaseResponse;
import com.emmilletta.ccs.client.asia.dto.AsiaDeregistrationRequest;
import com.emmilletta.ccs.client.asia.dto.AsiaRegistrationRequest;
import com.emmilletta.ccs.client.asia.dto.AsiaRegistrationResponse;
import com.emmilletta.ccs.client.asia.dto.AsiaStartChargingRequest;
import com.emmilletta.ccs.client.asia.dto.AsiaStartChargingResponse;
import com.emmilletta.ccs.client.asia.dto.AsiaStopChargingRequest;
import com.emmilletta.ccs.client.asia.dto.AsiaStopChargingResponse;
import com.emmilletta.ccs.client.europe.EuropeClient;
import com.emmilletta.ccs.client.europe.EuropeErrorCode;
import com.emmilletta.ccs.client.europe.dto.EuropeBaseResponse;
import com.emmilletta.ccs.client.europe.dto.EuropeDeregistrationRequest;
import com.emmilletta.ccs.client.europe.dto.EuropeRegistrationRequest;
import com.emmilletta.ccs.client.europe.dto.EuropeRegistrationResponse;
import com.emmilletta.ccs.client.europe.dto.EuropeStartChargingRequest;
import com.emmilletta.ccs.client.europe.dto.EuropeStartChargingResponse;
import com.emmilletta.ccs.client.europe.dto.EuropeStopChargingRequest;
import com.emmilletta.ccs.client.europe.dto.EuropeStopChargingResponse;
import com.emmilletta.ccs.client.japan.JapanClient;
import com.emmilletta.ccs.client.japan.JapanErrorCode;
import com.emmilletta.ccs.client.japan.dto.JapanBaseResponse;
import com.emmilletta.ccs.client.japan.dto.JapanDeregistrationRequest;
import com.emmilletta.ccs.client.japan.dto.JapanRegistrationRequest;
import com.emmilletta.ccs.client.japan.dto.JapanRegistrationResponse;
import com.emmilletta.ccs.client.japan.dto.JapanStartChargingRequest;
import com.emmilletta.ccs.client.japan.dto.JapanStartChargingResponse;
import com.emmilletta.ccs.client.japan.dto.JapanStopChargingRequest;
import com.emmilletta.ccs.client.japan.dto.JapanStopChargingResponse;
import com.emmilletta.ccs.client.vis.VisClient;
import com.emmilletta.ccs.client.vis.VisErrorCode;
import com.emmilletta.ccs.client.vis.dto.VisAccessTokenResponse;
import com.emmilletta.ccs.client.vis.dto.VisBaseResponse;
import com.emmilletta.ccs.dto.CcsRequest;
import com.emmilletta.ccs.dto.SourceSystem;
import com.emmilletta.ccs.dto.ChargingSessionResponse;
import com.emmilletta.ccs.dto.DeregistrationUserRequest;
import com.emmilletta.ccs.dto.GetUserInfoRequest;
import com.emmilletta.ccs.dto.RegistrationResponse;
import com.emmilletta.ccs.dto.RegistrationUserRequest;
import com.emmilletta.ccs.dto.RegistrationVehicle;
import com.emmilletta.ccs.dto.StartChargingRequest;
import com.emmilletta.ccs.dto.StartChargingResponse;
import com.emmilletta.ccs.dto.StopChargingRequest;
import com.emmilletta.ccs.dto.SessionStatus;
import com.emmilletta.ccs.dto.UserStatus;
import com.emmilletta.ccs.dto.CcsResponseStatus;
import com.emmilletta.ccs.dto.CcsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Alla Danko
 */
@Slf4j
@Component
public class Service {

    private final SessionRepository sRepository;
    private final UserRepository uRepository;
    private final AsiaClient asiaClient;
    private final EuropeClient europeClient;
    private final JapanClient japanClient;
    private final VisClient visClient;

    @Autowired
    public Service(SessionRepository sRepository,UserRepository uRepository,AsiaClient asiaClient,EuropeClient europeClient,JapanClient japanClient,VisClient visClient){
        this.sRepository=sRepository;
        this.uRepository=uRepository;
        this.asiaClient=asiaClient;
        this.europeClient=europeClient;
        this.japanClient=japanClient;
        this.visClient=visClient;
    }

    /**
     * Charging
     *
     * @return dto
     */
    @Transactional
    public CcsResponse<StartChargingResponse>charging(StartChargingRequest request,UserEntity user,VehicleEntity vehicle){
        CcsResponse<StartChargingResponse> ccs=null;
        switch(request.getProvider()){
            case ASIA:
                AsiaStartChargingRequest asiaStartChargingRequest=new AsiaStartChargingRequest();
                asiaStartChargingRequest.setProviderUserId(user.getProviderUserId());
                asiaStartChargingRequest.setVehicleId(request.getVehicleId());
                try {
                    ResponseEntity<AsiaStartChargingResponse> asiaStartChargingResponse=asiaClient.startCharging(asiaStartChargingRequest);
                    if (asiaStartChargingResponse.getStatusCode().is2xxSuccessful() && asiaStartChargingResponse.hasBody()) {
                        StartChargingResponse response = new StartChargingResponse();
                        response.setProvider(request.getProvider());
                        response.setSessionId(asiaStartChargingResponse.getBody().getSessionId());
                        response.setUserId(request.getUserId());
                        response.setVehicleId(request.getVehicleId());
                        response.setVendor(request.getVendor());
                        ccs = buildCcsResponse(CcsResponseStatus.CHARGING_STARTED, response);
                        break;}
                }catch(CException exception){
                    CcsResponseStatus responseMessage=AsiaErrorCode.getCcsResponseStatus(exception.getErrorCode());
                    return processGeneralCcsException(responseMessage, exception);
                }
                ccs=buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR, SourceSystem.ASIA, null);
                break;
            case EUROPE:
                try {
                    ResponseEntity<VisBaseResponse<VisAccessTokenResponse>> visResponse=visClient.getAccessToken(request.getVehicleId());
                    if(visResponse.getStatusCode().is2xxSuccessful() && visResponse.hasBody()){
                        EuropeStartChargingRequest europeRequest = new EuropeStartChargingRequest();
                        europeRequest.setAccessToken(visResponse.getBody().getData().getAccessToken());
                        europeRequest.setVehicleId(request.getVehicleId());
                        ResponseEntity<EuropeStartChargingResponse> europeResponse = europeClient.startCharging(europeRequest);
                        if(europeResponse.getStatusCode().is2xxSuccessful() && europeResponse.hasBody()){
                            StartChargingResponse response = new StartChargingResponse();
                            response.setProvider(request.getProvider());
                            response.setSessionId(europeResponse.getBody().getSessionId());
                            response.setUserId(request.getUserId());
                            response.setVehicleId(request.getVehicleId());
                            response.setVendor(request.getVendor());
                            ccs = buildCcsResponse(CcsResponseStatus.CHARGING_STARTED, response);
                            break;
                        }
                        ccs=buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR,
                                SourceSystem.EUROPE, null);
                        break;}
                }catch(CException exception){
                    CcsResponseStatus responseMessage = VisErrorCode.getCcsResponseStatus(exception.getErrorCode());
                    if (responseMessage==null){
                        responseMessage = EuropeErrorCode.getCcsResponseStatus(exception.getErrorCode());
                    }
                    return processGeneralCcsException(responseMessage, exception);
                }
                ccs=buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR, SourceSystem.VIS, null);
                break;
            case JAPAN:
                JapanStartChargingRequest japanRequest = new JapanStartChargingRequest();
                japanRequest.setVehicleId(request.getVehicleId());
                try {
                    ResponseEntity<JapanBaseResponse<JapanStartChargingResponse>> japanResponse = japanClient.startCharging(japanRequest);
                    if (japanResponse.getStatusCode().is2xxSuccessful() && japanResponse.hasBody()) {
                        StartChargingResponse response = new StartChargingResponse();
                        response.setProvider(request.getProvider());
                        response.setSessionId(japanResponse.getBody().getData().getSessionId());
                        response.setUserId(request.getUserId());
                        response.setVehicleId(request.getVehicleId());
                        response.setVendor(request.getVendor());
                        ccs = buildCcsResponse(CcsResponseStatus.CHARGING_STARTED, response);
                        break;}
                } catch (CException exception) {
                    CcsResponseStatus responseMessage = JapanErrorCode.getCcsResponseStatus(exception.getErrorCode());
                    return processGeneralCcsException(responseMessage, exception);}
                ccs = buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR, SourceSystem.JAPAN, null);
                break;
        }
        SessionEntity se=new SessionEntity();
        se.setProvider(request.getProvider());
        se.setSessionId(ccs.getData().getSessionId());
        se.setStartTime(ZonedDateTime.now());
        se.setStatus(SessionStatus.ACTIVE);
        se.setUser(user);
        se.setVehicle(vehicle);
        sRepository.save(se);
        return ccs;
    }

    /**
     * Charging
     *
     * @return dto
     */
    @Transactional
    public CcsResponse charging(StopChargingRequest request, UserEntity user){
        CcsResponse ccs = null;
        Optional<SessionEntity> session=sRepository.findActiveSessionByUserId(request.getUserId(), request.getSessionId());
        if(session.isPresent()){
            switch (session.get().getProvider()) {
                case ASIA:
                    AsiaStopChargingRequest asiaStopChargingRequest = new AsiaStopChargingRequest();
                    asiaStopChargingRequest.setProviderUserId(user.getProviderUserId());
                    asiaStopChargingRequest.setSessionId(request.getSessionId());
                    asiaStopChargingRequest.setVehicleId(request.getVehicleId());
                    try {
                        ResponseEntity<AsiaStopChargingResponse> asiaStopChargingResponse = asiaClient.stopCharging(asiaStopChargingRequest);
                        if (asiaStopChargingResponse.getStatusCode().is2xxSuccessful()){
                            ccs=buildCcsResponse(CcsResponseStatus.CHARGING_STOPPED, null);
                            break;
                        }
                    } catch (CException exception) {
                        CcsResponseStatus responseMessage = AsiaErrorCode.getCcsResponseStatus(exception.getErrorCode());
                        return processGeneralCcsException(responseMessage, exception);
                    }
                    ccs = buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR, SourceSystem.ASIA, null);
                    break;
                case EUROPE:
                    try {
                        ResponseEntity<VisBaseResponse<VisAccessTokenResponse>> visResponse=visClient.getAccessToken(request.getVehicleId());
                        if (visResponse.getStatusCode().is2xxSuccessful() && visResponse.hasBody()) {
                            EuropeStopChargingRequest europeRequest=new EuropeStopChargingRequest();
                            europeRequest.setAccessToken(visResponse.getBody().getData().getAccessToken());
                            europeRequest.setSessionId(request.getSessionId());
                            europeRequest.setVehicleId(request.getVehicleId());
                            ResponseEntity<EuropeStopChargingResponse> europeResponse = europeClient.stopCharging(europeRequest);
                            if (europeResponse.getStatusCode().is2xxSuccessful()) {
                                ccs=buildCcsResponse(CcsResponseStatus.CHARGING_STOPPED, null);
                                break;}
                            ccs = buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR,
                                    SourceSystem.EUROPE, null);
                            break;
                        }
                    } catch (CException exception) {
                        CcsResponseStatus responseMessage = VisErrorCode.getCcsResponseStatus(exception.getErrorCode());
                        if (responseMessage==null) {
                            responseMessage=EuropeErrorCode.getCcsResponseStatus(exception.getErrorCode());
                        }
                        return processGeneralCcsException(responseMessage, exception);
                    }
                    ccs = buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR, SourceSystem.VIS, null);
                    break;
                case JAPAN:
                    JapanStopChargingRequest japanRequest=new JapanStopChargingRequest();
                    japanRequest.setSessionId(request.getSessionId());
                    try {
                        ResponseEntity<JapanBaseResponse<JapanStopChargingResponse>> japanResponse = japanClient.stopCharging(japanRequest);
                        if (japanResponse.getStatusCode().is2xxSuccessful()) {
                            ccs=buildCcsResponse(CcsResponseStatus.CHARGING_STOPPED, null);
                            break;
                        }
                    } catch (CException exception) {
                        CcsResponseStatus responseMessage=JapanErrorCode.getCcsResponseStatus(exception.getErrorCode());
                        return processGeneralCcsException(responseMessage, exception);
                    }
                    ccs=buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR, SourceSystem.JAPAN, null);
                    break;
            }
            SessionEntity se=session.get();
            se.setStatus(SessionStatus.INACTIVE);
            se.setStopTime(ZonedDateTime.now());
            sRepository.save(se);
            return ccs;
        }
        return buildCcsResponse(CcsResponseStatus.NO_ACTIVE_SESSION, null);
    }

    /**
     * Sessions
     *
     * @return response
     */
    public CcsResponse<List<ChargingSessionResponse>>sessions(SessionStatus status, CcsRequest request, UserEntity user){
        List<ChargingSessionResponse> result = new ArrayList<>();
        for (SessionEntity session:user.getSessions()) {
            if (status!=null || status.equals(session.getStatus())) {
                ChargingSessionResponse response = new ChargingSessionResponse();
                response.setProvider(session.getProvider());
                response.setSessionId(session.getSessionId());
                response.setStartTime(session.getStartTime());
                response.setStopTime(session.getStopTime());
                response.setUserId(session.getUser().getUserId());
                response.setVehicleId(session.getVehicle().getVehicleId());
                response.setStatus(session.getStatus());
                result.add(response);
            }
        }
        return buildCcsResponse(CcsResponseStatus.SESSION_RETURNED, result);
    }

    /**
     * Registration
     *
     * @return dto
     */
    @Transactional
    public CcsResponse<RegistrationResponse>registration(RegistrationUserRequest request){
        CcsResponse<RegistrationResponse> ccs=null;
        UserEntity newUser=user(request);
        switch(request.getProvider()){
            case ASIA:
                AsiaRegistrationRequest asiaRegistrationRequest = new AsiaRegistrationRequest();
                asiaRegistrationRequest.setEmail(request.getEmail());
                if (request.getVehicle()!=null) {
                    asiaRegistrationRequest.setVehicle(request.getVehicle().getVehicleId());
                }
                try {
                    ResponseEntity<AsiaRegistrationResponse> asiaRegistrationResponse = asiaClient.registration(asiaRegistrationRequest);
                    if (asiaRegistrationResponse.getStatusCode().is2xxSuccessful() && asiaRegistrationResponse.hasBody()) {
                        newUser.setProviderUserId(asiaRegistrationResponse.getBody().getProviderUserId());
                        ccs = buildCcsResponse(CcsResponseStatus.USER_REGISTERED, null);
                        break;
                    }
                }catch(CException exception){
                    CcsResponseStatus responseMessage = AsiaErrorCode.getCcsResponseStatus(exception.getErrorCode());
                    return processGeneralCcsException(responseMessage, exception);
                }
                ccs = buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR, SourceSystem.ASIA, null);
                break;
            case EUROPE:
                EuropeRegistrationRequest europeRequest=new EuropeRegistrationRequest();
                europeRequest.setEmail(request.getEmail());
                if(request.getVehicle()!=null){
                    europeRequest.setVehicle(request.getVehicle().getVehicleId());
                }
                try {
                    ResponseEntity<EuropeRegistrationResponse> europeResponse = europeClient.registerUser(europeRequest);
                    if (europeResponse.getStatusCode().is2xxSuccessful()) {
                        ccs = buildCcsResponse(CcsResponseStatus.USER_REGISTERED, null);
                        break;
                    }
                } catch(CException exception){
                    CcsResponseStatus responseMessage = VisErrorCode.getCcsResponseStatus(exception.getErrorCode());
                    if(responseMessage==null){
                        responseMessage=EuropeErrorCode.getCcsResponseStatus(exception.getErrorCode());
                    }
                    return processGeneralCcsException(responseMessage, exception);
                }
                ccs = buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR, SourceSystem.EUROPE, null);
                break;
            case JAPAN:
                JapanRegistrationRequest japanRequest = new JapanRegistrationRequest();
                japanRequest.setEmail(request.getEmail());
                if (request.getVehicle()!=null) {
                    japanRequest.setVehicle(request.getVehicle().getVehicleId());}
                try {
                    ResponseEntity<JapanBaseResponse<JapanRegistrationResponse>> japanResponse = japanClient.activateAccount(japanRequest);
                    if (japanResponse.getStatusCode().is2xxSuccessful()) {
                        newUser.setUserAccessKey(japanResponse.getBody().getData().getUserAccessKey());
                        ccs = buildCcsResponse(CcsResponseStatus.USER_REGISTERED, null);
                        break;
                    }
                } catch(CException exception){
                    CcsResponseStatus responseMessage = JapanErrorCode.getCcsResponseStatus(exception.getErrorCode());
                    return processGeneralCcsException(responseMessage, exception);
                }
                ccs = buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR, SourceSystem.JAPAN, null);
                break;
        }
        if (ccs.getResponseCode().getHttpStatus().is2xxSuccessful()) {
            RegistrationResponse response = new RegistrationResponse();
            response.setEmail(newUser.getEmail());
            response.setRegistrationDate(newUser.getRegistrationDate());
            response.setUserId(newUser.getUserId());
            response.setVehicles(new ArrayList<>());
            for(VehicleEntity vehicleEntity:newUser.getVehicles()){
                RegistrationVehicle registrationVehicle = new RegistrationVehicle();
                registrationVehicle.setVehicleId(vehicleEntity.getVehicleId());
                registrationVehicle.setVendor(vehicleEntity.getVendor());
                response.getVehicles().add(registrationVehicle);
            }
            uRepository.save(newUser);
            ccs.setData(response);
            return ccs;
        }
        return ccs;
    }

    /**
     * Registration
     *
     * @return dto
     */
    @Transactional
    public CcsResponse<RegistrationResponse> registration(DeregistrationUserRequest request, UserEntity user) {
        CcsResponse<RegistrationResponse> ccs = null;
        switch (request.getProvider()){
            case ASIA:
                AsiaDeregistrationRequest asiaDeregistrationRequest = new AsiaDeregistrationRequest();
                asiaDeregistrationRequest.setProviderUserId(user.getProviderUserId());
                asiaDeregistrationRequest.setVehicle(request.getVehicle()==null?null:request.getVehicle().getVehicleId());
                try {
                    ResponseEntity<AsiaBaseResponse> asiaRegistrationResponse = asiaClient.deregistration(asiaDeregistrationRequest);
                    if (asiaRegistrationResponse.getStatusCode().is2xxSuccessful()) {
                        ccs=buildCcsResponse(CcsResponseStatus.USER_DEREGISTERED, null);
                        break;
                    }
                } catch (CException exception) {
                    CcsResponseStatus responseMessage=AsiaErrorCode.getCcsResponseStatus(exception.getErrorCode());
                    return processGeneralCcsException(responseMessage, exception);
                }
                ccs = buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR, SourceSystem.ASIA, null);
                break;
            case EUROPE:
                if (request.getVehicle()==null || request.getVehicle().getVehicleId()==null){
                    ccs = buildCcsResponseFromSourceSystem(CcsResponseStatus.WRONG_ACCESS_TOKEN, SourceSystem.EUROPE, null);
                    break;
                }
                try {
                    ResponseEntity<VisBaseResponse<VisAccessTokenResponse>> visResponse =visClient.getAccessToken(request.getVehicle().getVehicleId());
                    if(visResponse.getStatusCode().is2xxSuccessful() && visResponse.hasBody()){
                        EuropeDeregistrationRequest europeRequest= new EuropeDeregistrationRequest();
                        europeRequest.setAccessToken(visResponse.getBody().getData().getAccessToken());
                        europeRequest.setVehicle(request.getVehicle() ==null?null:request.getVehicle().getVehicleId());
                        ResponseEntity<EuropeBaseResponse> europeResponse =europeClient.deregisterUser(europeRequest);
                        if(europeResponse.getStatusCode().is2xxSuccessful()){
                            ccs = buildCcsResponse(CcsResponseStatus.USER_DEREGISTERED, null);
                            break;
                        }
                        ccs = buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR, SourceSystem.EUROPE, null);
                        break;
                    }
                } catch (CException exception) {
                    CcsResponseStatus responseMessage = VisErrorCode.getCcsResponseStatus(exception.getErrorCode());
                    if (responseMessage == null) {
                        responseMessage = EuropeErrorCode.getCcsResponseStatus(exception.getErrorCode());
                    }
                    return processGeneralCcsException(responseMessage, exception);
                }
                ccs = buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR, SourceSystem.VIS, null);
                break;
            case JAPAN:
                JapanDeregistrationRequest japanRequest=new JapanDeregistrationRequest();
                japanRequest.setVehicleId(request.getVehicle()==null?null:request.getVehicle().getVehicleId());
                try {
                    ResponseEntity<JapanBaseResponse> japanResponse = japanClient.deactivateAccount(japanRequest);
                    if(japanResponse.getStatusCode().is2xxSuccessful()){
                        ccs=buildCcsResponse(CcsResponseStatus.USER_DEREGISTERED, null);
                        break;
                    }
                }catch(CException exception){
                    CcsResponseStatus responseMessage = JapanErrorCode.getCcsResponseStatus(exception.getErrorCode());
                    return processGeneralCcsException(responseMessage, exception);
                }
                ccs = buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR, SourceSystem.JAPAN, null);
                break;
        }
        if(ccs.getResponseCode().getHttpStatus().is2xxSuccessful()){
            if(request.getVehicle()!=null){
                List<VehicleEntity> vehicles = new ArrayList<>();
                for (VehicleEntity vehicle:user.getVehicles()) {
                    if (request.getVehicle().getVehicleId().contains(vehicle.getVehicleId())) {
                        vehicles.add(vehicle);
                    }
                }
                user.getVehicles().removeAll(vehicles);
                if (user.getVehicles().isEmpty()) {
                    user.setStatus(UserStatus.INACTIVE); }
            } else {
                user.setStatus(UserStatus.INACTIVE);}
            RegistrationResponse response=new RegistrationResponse();
            response.setEmail(user.getEmail());
            response.setRegistrationDate(user.getRegistrationDate());
            response.setUserId(user.getUserId());
            response.setVehicles(new ArrayList<>());
            for(VehicleEntity vehicleEntity:user.getVehicles()){
                RegistrationVehicle registrationVehicle= new RegistrationVehicle();
                registrationVehicle.setVehicleId(vehicleEntity.getVehicleId());
                registrationVehicle.setVendor(vehicleEntity.getVendor());
                response.getVehicles().add(registrationVehicle);
            }
            uRepository.save(user);
            ccs.setData(response);
            return ccs;
        }
        return ccs;
    }

    /**
     * Info
     *
     * @return response
     */
    public CcsResponse<List<RegistrationResponse>> info(GetUserInfoRequest request) {
        List<UserEntity> resultUsers = new ArrayList<>();
        if(request.getUserId()!=null) {
            Optional<UserEntity> user=uRepository.findByUserId(request.getUserId());
            if (user.isPresent()){
                resultUsers.add(user.get());}
        } else {
            resultUsers = uRepository.findByStatusAndDates(request.getStatus(), request.getStartDate().atStartOfDay(ZoneOffset.UTC), request.getStopDate().atStartOfDay(ZoneOffset.UTC));}
        List<RegistrationResponse> response =new ArrayList<>();
        if(!resultUsers.isEmpty()) {
            for(UserEntity user: resultUsers) {
                RegistrationResponse registrationResponse= new RegistrationResponse();
                registrationResponse.setUserId(user.getUserId());
                registrationResponse.setRegistrationDate(user.getRegistrationDate());
                registrationResponse.setEmail(user.getEmail());
                registrationResponse.setVehicles(new ArrayList<>());
                for(VehicleEntity vehicleEntity:user.getVehicles()){
                    RegistrationVehicle vehicle= new RegistrationVehicle();
                    vehicle.setVehicleId(vehicleEntity.getVehicleId());
                    vehicle.setVendor(vehicleEntity.getVendor());
                    registrationResponse.getVehicles().add(vehicle);
                }
            }
        }
        return buildCcsResponse(CcsResponseStatus.USER_INFO_RETURNED, response);
    }


    private UserEntity user(RegistrationUserRequest request) {
        Optional<UserEntity> oldUser = uRepository.findByEmail(request.getEmail());
        UserEntity updatedUser;
        if (oldUser.isPresent()){
            updatedUser=oldUser.get();
        } else{
            updatedUser=new UserEntity();
            updatedUser.setVehicles(new ArrayList<>());}
        updatedUser.setUserId(RandomStringUtils.randomAlphanumeric(16));
        updatedUser.setEmail(request.getEmail());
        updatedUser.setRegistrationDate(ZonedDateTime.now());
        updatedUser.setStatus(UserStatus.ACTIVE);
        if (request.getVehicle()!=null){
            if (updatedUser.getVehicles().isEmpty()){
                VehicleEntity vehicleEntity=new VehicleEntity();
                vehicleEntity.setVehicleId(request.getVehicle().getVehicleId());
                vehicleEntity.setUser(updatedUser);
                vehicleEntity.setVendor(request.getVehicle().getVendor());
                updatedUser.getVehicles().add(vehicleEntity);
            } else {
                VehicleEntity oldVehicle=null;
                for (VehicleEntity updatedVehicle: updatedUser.getVehicles()){
                    if (updatedVehicle.getVehicleId().equals(request.getVehicle().getVehicleId())){
                        oldVehicle =updatedVehicle;
                    }}
                VehicleEntity newVehicle;
                if (oldVehicle==null) {
                    newVehicle=new VehicleEntity();
                    updatedUser.getVehicles().add(newVehicle);
                } else {
                    newVehicle =oldVehicle;}
                newVehicle.setVehicleId(request.getVehicle().getVehicleId());
                newVehicle.setVendor(request.getVehicle().getVendor());
                newVehicle.setUser(updatedUser);
            }
        }
        return updatedUser;
    }

    public CcsResponse processGeneralCcsException(CcsResponseStatus responseMessage, CException exception){
        if (responseMessage !=null) {
            return buildCcsResponseFromSourceSystem(responseMessage, exception.getSourceSystem(), null);
        }
        log.error("Ccs exception: {}. {}",exception.getMessage(),exception.getStackTrace());
        return buildCcsResponseFromSourceSystem(CcsResponseStatus.UNKNOWN_RESOURCE_ERROR, exception.getSourceSystem(), null);

    }

    public <T> CcsResponse<T> buildCcsResponse(CcsResponseStatus status, T data){
        CcsResponse ccs=new CcsResponse();
        ccs.setData(data);
        ccs.setMessage(status.getMessage());
        ccs.setResponseCode(status);
        ccs.setSource(SourceSystem.CCS);
        return ccs;
    }

    public <T> CcsResponse<T> buildCcsResponseFromSourceSystem(CcsResponseStatus status, SourceSystem sourceSystem, T data){
        CcsResponse ccs=new CcsResponse();
        ccs.setData(data);
        ccs.setMessage(status.getMessage());
        ccs.setResponseCode(status);
        ccs.setSource(sourceSystem);
        return ccs;
    }
}
