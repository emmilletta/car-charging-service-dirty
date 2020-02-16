package com.emmilletta.ccs;

import com.emmilletta.ccs.dto.StartChargingRequest;
import com.emmilletta.ccs.dto.StopChargingRequest;
import com.emmilletta.ccs.dto.CcsRequest;
import com.emmilletta.ccs.dto.CcsResponse;
import com.emmilletta.ccs.dto.CcsResponseStatus;
import com.emmilletta.ccs.dto.ProviderEnum;
import com.emmilletta.ccs.dto.SessionStatus;
import com.emmilletta.ccs.dto.UserStatus;
import com.emmilletta.ccs.dto.VendorEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Alla Danko
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ChargingEndpointIT {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clean() {
        userRepository.deleteAll();
        UserEntity user = UserEntity.builder().userId("testUserId").userAccessKey("testUserAccessKey")
                .email("testUserEmail1@gmail.com").registrationDate(ZonedDateTime.now()).status(UserStatus.ACTIVE).build();
        user.setVehicles(Arrays.asList(VehicleEntity.builder().vehicleId("testVehicleId").user(user).vendor(VendorEnum.BMW).build()));
        userRepository.save(user);
    }

    @Test
    void startChargingNullProvider() throws Exception {
        StartChargingRequest request = createStartRequest();
        request.setProvider(null);

        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/start")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.MISSING_PARAMETER, content.getResponseCode());
        Assert.assertTrue(content.getMessage().contains("provider"));
    }

    @Test
    void startChargingNullUser() throws Exception {
        StartChargingRequest request = createStartRequest();
        request.setUserId(null);

        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/start")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.MISSING_PARAMETER, content.getResponseCode());
        Assert.assertTrue(content.getMessage().contains("userId"));
    }

    @Test
    void startChargingNullVehicle() throws Exception {
        StartChargingRequest request = createStartRequest();
        request.setVehicleId(null);

        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/start")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.MISSING_PARAMETER, content.getResponseCode());
        Assert.assertTrue(content.getMessage().contains("vehicleId"));
    }

    @Test
    void startChargingUnknownUser() throws Exception {
        StartChargingRequest request = createStartRequest();
        request.setUserId("unknownTestUserId");

        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/start")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.USER_NOT_REGISTERED, content.getResponseCode());
    }

    @Test
    void startChargingUnknownVehicle() throws Exception {
        StartChargingRequest request = createStartRequest();
        request.setVehicleId("unknownTestVehicleId");

        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/start")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.VEHICLE_NOT_REGISTERED, content.getResponseCode());
    }

    @Test
    void stopChargingNoActiveSession() throws Exception {
        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/stop")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createStopRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.NO_ACTIVE_SESSION, content.getResponseCode());
    }

    @Test
    void getSessionsSuccessful() throws Exception {
        CcsRequest request = CcsRequest.builder()
                .userId("testUserId")
                .vehicleId("testVehicleId")
                .build();
        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/sessions" + "?status=" + SessionStatus.ACTIVE)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.SESSION_RETURNED, content.getResponseCode());
    }

    private StartChargingRequest createStartRequest() {
        return StartChargingRequest.builder()
                .provider(ProviderEnum.ASIA)
                .userId("testUserId")
                .vehicleId("testVehicleId")
                .vendor(VendorEnum.BMW)
                .build();
    }

    private StopChargingRequest createStopRequest() {
        return StopChargingRequest.builder()
                .sessionId("sessionId")
                .provider(ProviderEnum.ASIA)
                .userId("testUserId")
                .vehicleId("testVehicleId")
                .build();
    }
}