package com.emmilletta.ccs.client.vis;

import com.emmilletta.ccs.client.vis.dto.VisAccessTokenResponse;
import com.emmilletta.ccs.client.vis.dto.VisBaseResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Alla Danko
 */
public interface Vis {

    String API_NAME = "/vehicles";
    String API_VERSION = "/1.3";
    String API_PATH = API_NAME + API_VERSION;

    @GetMapping(value = API_PATH + "/getAccessToken", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<VisBaseResponse<VisAccessTokenResponse>> getAccessToken(@RequestParam String vehicleId);
}
