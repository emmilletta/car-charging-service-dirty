package com.emmilletta.ccs.client.europe;

import com.emmilletta.ccs.client.europe.dto.EuropeBaseResponse;
import com.emmilletta.ccs.client.europe.dto.EuropeDeregistrationRequest;
import com.emmilletta.ccs.client.europe.dto.EuropeRegistrationRequest;
import com.emmilletta.ccs.client.europe.dto.EuropeRegistrationResponse;
import com.emmilletta.ccs.client.europe.dto.EuropeStartChargingRequest;
import com.emmilletta.ccs.client.europe.dto.EuropeStartChargingResponse;
import com.emmilletta.ccs.client.europe.dto.EuropeStopChargingRequest;
import com.emmilletta.ccs.client.europe.dto.EuropeStopChargingResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Alla Danko
 */
public interface Europe {

    String API_NAME = "/europe";
    String API_VERSION = "/1.0";
    String API_PATH = API_NAME + API_VERSION;

    @PostMapping(value = API_PATH + "/startCharging", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<EuropeStartChargingResponse> startCharging(@RequestBody EuropeStartChargingRequest request);

    @PostMapping(value = API_PATH + "/stopCharging", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<EuropeStopChargingResponse> stopCharging(@RequestBody EuropeStopChargingRequest request);

    @PostMapping(value = API_PATH + "/registerUser", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<EuropeRegistrationResponse> registerUser(@RequestBody EuropeRegistrationRequest request);

    @PutMapping(value = API_PATH + "/deregisterUser", produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<EuropeBaseResponse> deregisterUser(@RequestBody EuropeDeregistrationRequest request);
}
