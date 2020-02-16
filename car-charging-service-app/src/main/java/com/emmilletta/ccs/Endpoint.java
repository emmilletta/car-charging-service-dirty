package com.emmilletta.ccs;

import com.emmilletta.ccs.dto.CcsResponseStatus;
import com.emmilletta.ccs.dto.DeregistrationUserRequest;
import com.emmilletta.ccs.dto.GetUserInfoRequest;
import com.emmilletta.ccs.dto.RegistrationResponse;
import com.emmilletta.ccs.dto.RegistrationUserRequest;
import com.emmilletta.ccs.dto.ChargingSessionResponse;
import com.emmilletta.ccs.dto.SourceSystem;
import com.emmilletta.ccs.dto.StartChargingRequest;
import com.emmilletta.ccs.dto.StartChargingResponse;
import com.emmilletta.ccs.dto.StopChargingRequest;
import com.emmilletta.ccs.dto.CcsRequest;
import com.emmilletta.ccs.dto.CcsResponse;
import com.emmilletta.ccs.dto.SessionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author Alla Danko
 */
@Slf4j
@RestController
public class Endpoint implements CcsApi {

    private Service service;
    private UserRepository uRepository;

    @Autowired
    public Endpoint(Service service, UserRepository uRepository) {
        this.service = service;
        this.uRepository = uRepository;
    }

    @Override
    public ResponseEntity<CcsResponse<StartChargingResponse>> charging(
            @RequestBody @Validated StartChargingRequest request) {
        CcsResponse ccs; VehicleEntity v=null;
        Optional<UserEntity> user=uRepository.findByActiveUserId(request.getUserId());
        if (user.isPresent()) {
            for (VehicleEntity vehicle:user.get().getVehicles())
                if (vehicle.getVehicleId().equals(request.getVehicleId())) {
                    v=vehicle;
                    break;
                }
            if (v!=null)
                ccs=service.charging(request, user.get(), v);
            else
                ccs=buildCcsResponse(CcsResponseStatus.VEHICLE_NOT_REGISTERED, null);
        } else
            ccs=buildCcsResponse(CcsResponseStatus.USER_NOT_REGISTERED, null);
        return ResponseEntity.status(ccs.getResponseCode().getHttpStatus()).body(ccs);
    }

    @Override
    public ResponseEntity<CcsResponse> charging(@RequestBody @Validated StopChargingRequest request) {
        CcsResponse ccs; VehicleEntity v=null;
        Optional<UserEntity> user=uRepository.findByActiveUserId(request.getUserId());
        if (user.isPresent()) {
            for (VehicleEntity vehicle:user.get().getVehicles())
                if (vehicle.getVehicleId().equals(request.getVehicleId())) {
                    v=vehicle;
                    break;
                }
            if (v!=null)
                ccs=service.charging(request, user.get());
            else
                ccs=buildCcsResponse(CcsResponseStatus.VEHICLE_NOT_REGISTERED, null);
        } else
            ccs=buildCcsResponse(CcsResponseStatus.USER_NOT_REGISTERED, null);
        return ResponseEntity.status(ccs.getResponseCode().getHttpStatus()).body(ccs);
    }

    @Override
    public ResponseEntity<CcsResponse<List<ChargingSessionResponse>>>sessions(@RequestParam(value = "status", required = false) SessionStatus status, @RequestBody @Validated CcsRequest request) {
        CcsResponse ccs;VehicleEntity v=null;
        Optional<UserEntity> user=uRepository.findByActiveUserId(request.getUserId());
        if (user.isPresent()) {
            for (VehicleEntity vehicle:user.get().getVehicles())
                if (vehicle.getVehicleId().equals(request.getVehicleId())) {
                    v=vehicle;
                    break;
                }
            if (v!=null)
                ccs=service.sessions(status, request, user.get());
            else
                ccs=buildCcsResponse(CcsResponseStatus.VEHICLE_NOT_REGISTERED, null);
        } else
            ccs=buildCcsResponse(CcsResponseStatus.USER_NOT_REGISTERED, null);
        return ResponseEntity.status(ccs.getResponseCode().getHttpStatus()).body(ccs);
    }

    @Override
    public ResponseEntity<CcsResponse<RegistrationResponse>> registration(@RequestBody @Validated RegistrationUserRequest request) {
        CcsResponse<RegistrationResponse> ccs=service.registration(request);
        return ResponseEntity.status(ccs.getResponseCode().getHttpStatus()).body(ccs);
    }

    @Override
    public ResponseEntity<CcsResponse<RegistrationResponse>> registration(@RequestBody @Validated DeregistrationUserRequest request) {
        CcsResponse ccs;
        Optional<UserEntity> userOptional=uRepository.findByActiveUserId(request.getUserId());
        if(userOptional.isPresent())
             ccs=service.registration(request, userOptional.get());
        else
            ccs=buildCcsResponse(CcsResponseStatus.USER_NOT_REGISTERED, null);
        return ResponseEntity.status(ccs.getResponseCode().getHttpStatus()).body(ccs);
    }

    @Override
    public ResponseEntity<CcsResponse<List<RegistrationResponse>>> info(@RequestBody @Validated GetUserInfoRequest request) {
        CcsResponse<List<RegistrationResponse>> ccs=service.info(request);
        return ResponseEntity.status(ccs.getResponseCode().getHttpStatus()).body(ccs);
    }

    public <T> CcsResponse<T> buildCcsResponse(CcsResponseStatus status, T data) {
        CcsResponse ccs=new CcsResponse(); ccs.setData(data); ccs.setMessage(status.getMessage()); ccs.setResponseCode(status); ccs.setSource(SourceSystem.CCS);
        return ccs;
    }
}
