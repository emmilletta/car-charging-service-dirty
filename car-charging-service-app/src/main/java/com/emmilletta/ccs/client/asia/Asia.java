package com.emmilletta.ccs.client.asia;

import com.emmilletta.ccs.client.asia.dto.AsiaBaseResponse;
import com.emmilletta.ccs.client.asia.dto.AsiaDeregistrationRequest;
import com.emmilletta.ccs.client.asia.dto.AsiaRegistrationRequest;
import com.emmilletta.ccs.client.asia.dto.AsiaRegistrationResponse;
import com.emmilletta.ccs.client.asia.dto.AsiaStartChargingRequest;
import com.emmilletta.ccs.client.asia.dto.AsiaStartChargingResponse;
import com.emmilletta.ccs.client.asia.dto.AsiaStopChargingRequest;
import com.emmilletta.ccs.client.asia.dto.AsiaStopChargingResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Alla Danko
 */
public interface Asia {

    String API_NAME = "/asia";
    String API_VERSION = "/1.0";
    String API_PATH = API_NAME + API_VERSION;

    @PostMapping(value = API_PATH + "/start-session", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<AsiaStartChargingResponse> startCharging(@RequestBody AsiaStartChargingRequest request);

    @PostMapping(value = API_PATH + "/stopSession", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<AsiaStopChargingResponse> stopCharging(@RequestBody AsiaStopChargingRequest request);

    @PostMapping(value = API_PATH + "/registration", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<AsiaRegistrationResponse> registration(@RequestBody AsiaRegistrationRequest request);

    @PutMapping(value = API_PATH + "/deregistration", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<AsiaBaseResponse> deregistration(@RequestBody AsiaDeregistrationRequest request);
}
