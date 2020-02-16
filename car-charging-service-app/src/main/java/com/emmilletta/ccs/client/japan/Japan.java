package com.emmilletta.ccs.client.japan;

import com.emmilletta.ccs.client.japan.dto.JapanBaseResponse;
import com.emmilletta.ccs.client.japan.dto.JapanDeregistrationRequest;
import com.emmilletta.ccs.client.japan.dto.JapanRegistrationRequest;
import com.emmilletta.ccs.client.japan.dto.JapanRegistrationResponse;
import com.emmilletta.ccs.client.japan.dto.JapanStartChargingRequest;
import com.emmilletta.ccs.client.japan.dto.JapanStartChargingResponse;
import com.emmilletta.ccs.client.japan.dto.JapanStopChargingRequest;
import com.emmilletta.ccs.client.japan.dto.JapanStopChargingResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Alla Danko
 */
public interface Japan {

    String API_NAME = "/japan";
    String API_VERSION = "/2.0";
    String API_PATH = API_NAME + API_VERSION;

    @PostMapping(value = API_PATH + "/startSession", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<JapanBaseResponse<JapanStartChargingResponse>> startCharging(@RequestBody JapanStartChargingRequest request);

    @PostMapping(value = API_PATH + "/stopSession", produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<JapanBaseResponse<JapanStopChargingResponse>> stopCharging(@RequestBody JapanStopChargingRequest request);

    @PostMapping(value = API_PATH + "/activateAccount", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<JapanBaseResponse<JapanRegistrationResponse>> activateAccount(@RequestBody JapanRegistrationRequest request);

    @PutMapping(value = API_PATH + "/deactivateAccount", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<JapanBaseResponse> deactivateAccount(@RequestBody JapanDeregistrationRequest request);
}
