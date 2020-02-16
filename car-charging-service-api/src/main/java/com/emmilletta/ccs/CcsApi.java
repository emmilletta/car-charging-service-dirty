package com.emmilletta.ccs;

import com.emmilletta.ccs.dto.ChargingSessionResponse;
import com.emmilletta.ccs.dto.StartChargingRequest;
import com.emmilletta.ccs.dto.StartChargingResponse;
import com.emmilletta.ccs.dto.StopChargingRequest;
import com.emmilletta.ccs.dto.DeregistrationUserRequest;
import com.emmilletta.ccs.dto.GetUserInfoRequest;
import com.emmilletta.ccs.dto.RegistrationResponse;
import com.emmilletta.ccs.dto.RegistrationUserRequest;
import com.emmilletta.ccs.dto.CcsRequest;
import com.emmilletta.ccs.dto.CcsResponse;
import com.emmilletta.ccs.dto.SessionStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Alla Danko
 */
public interface CcsApi {

    String apiPath = "/v1";

    @PostMapping(path = apiPath + "/start")
    ResponseEntity<CcsResponse<StartChargingResponse>> charging(@RequestBody StartChargingRequest request);

    @PostMapping(path = apiPath + "/stop")
    ResponseEntity<CcsResponse> charging(@RequestBody StopChargingRequest request);

    @PostMapping(path = apiPath + "/sessions")
    ResponseEntity<CcsResponse<List<ChargingSessionResponse>>> sessions(
            @RequestParam(value = "status", required = false) SessionStatus status,
            @RequestBody CcsRequest request);

    @PostMapping(path = apiPath + "/register")
    ResponseEntity<CcsResponse<RegistrationResponse>> registration(@RequestBody RegistrationUserRequest request);

    @PutMapping(path = apiPath + "/deregister")
    ResponseEntity<CcsResponse<RegistrationResponse>> registration(@RequestBody DeregistrationUserRequest request);

    @PostMapping(path = apiPath + "/getUserInfo")
    ResponseEntity<CcsResponse<List<RegistrationResponse>>> info(@RequestBody GetUserInfoRequest request);
}
