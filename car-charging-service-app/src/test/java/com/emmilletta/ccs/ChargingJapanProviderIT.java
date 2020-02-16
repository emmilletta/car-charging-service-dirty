package com.emmilletta.ccs;

import com.emmilletta.ccs.dto.StartChargingRequest;
import com.emmilletta.ccs.dto.StartChargingResponse;
import com.emmilletta.ccs.dto.StopChargingRequest;
import com.emmilletta.ccs.dto.CcsResponse;
import com.emmilletta.ccs.dto.CcsResponseStatus;
import com.emmilletta.ccs.dto.ProviderEnum;
import com.emmilletta.ccs.dto.SessionStatus;
import com.emmilletta.ccs.dto.UserStatus;
import com.emmilletta.ccs.dto.VendorEnum;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Alla Danko
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ChargingJapanProviderIT {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    public void clean() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void startChargingSuccessful() throws Exception {
        UserEntity user = createUser();
        userRepository.save(user);

        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/start")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createStartRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CcsResponse<StartChargingResponse> content = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<CcsResponse<StartChargingResponse>>() {
                });
        Assert.assertEquals(CcsResponseStatus.CHARGING_STARTED, content.getResponseCode());
        Assert.assertTrue(sessionRepository.findActiveSessionByUserId("testUserId",
                content.getData().getSessionId()).isPresent());
    }

    @Test
    void startChargingStationNotAvailable() throws Exception {
        String errorCode = "station_not_available";

        UserEntity user = createUser();
        user.getVehicles().get(0).setVehicleId(errorCode);
        userRepository.save(user);

        StartChargingRequest request = createStartRequest();
        request.setVehicleId(errorCode);

        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/start")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.STATION_NOT_AVAILABLE, content.getResponseCode());
        Assert.assertTrue(sessionRepository.findAll().isEmpty());
    }

    @Test
    void startChargingInternalServerError() throws Exception {
        String errorCode = "internal_server_error";

        UserEntity user = createUser();
        user.getVehicles().get(0).setVehicleId(errorCode);
        userRepository.save(user);

        StartChargingRequest request = createStartRequest();
        request.setVehicleId(errorCode);

        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/start")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.INTERNAL_SERVER_ERROR, content.getResponseCode());
        Assert.assertTrue(sessionRepository.findAll().isEmpty());
    }

    @Test
    void startChargingVehicleNotFound() throws Exception {
        String errorCode = "vehicle_not_found";

        UserEntity user = createUser();
        user.getVehicles().get(0).setVehicleId(errorCode);
        userRepository.save(user);

        StartChargingRequest request = createStartRequest();
        request.setVehicleId(errorCode);

        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/start")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.VEHICLE_NOT_FOUND, content.getResponseCode());
        Assert.assertTrue(sessionRepository.findAll().isEmpty());
    }

    @Test
    void startChargingVehicleNotConnected() throws Exception {
        String errorCode = "vehicle_not_connected";

        UserEntity user = createUser();
        user.getVehicles().get(0).setVehicleId(errorCode);
        userRepository.save(user);

        StartChargingRequest request = createStartRequest();
        request.setVehicleId(errorCode);

        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/start")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.VEHICLE_NOT_CONNECTED, content.getResponseCode());
        Assert.assertTrue(sessionRepository.findAll().isEmpty());
    }

    @Test
    void stopChargingSuccessful() throws Exception {
        UserEntity user = createUser();
        userRepository.save(user);

        SessionEntity session = createSession(user);
        sessionRepository.save(session);

        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/stop")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createStopRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.CHARGING_STOPPED, content.getResponseCode());
        List<SessionEntity> sessions = sessionRepository.findAll();
        Assert.assertEquals(1, sessions.size());
    }

    @Test
    void stopChargingInvalidSessionId() throws Exception {
        String errorCode = "invalid_session_id";

        UserEntity user = createUser();
        userRepository.save(user);

        SessionEntity session = createSession(user);
        session.setSessionId(errorCode);
        sessionRepository.save(session);

        StopChargingRequest request = createStopRequest();
        request.setSessionId(errorCode);

        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/stop")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.INVALID_SESSION_ID, content.getResponseCode());
        Assert.assertTrue(sessionRepository.findActiveSessionByUserId("testUserId",
                "invalid_session_id").isPresent());
    }

    private UserEntity createUser() {
        UserEntity user = UserEntity.builder().userId("testUserId").userAccessKey("testUserAccessKey")
                .email("testUserEmail1@gmail.com").registrationDate(ZonedDateTime.now()).status(UserStatus.ACTIVE).build();
        user.setVehicles(Arrays.asList(VehicleEntity.builder().vehicleId("testVehicleId").user(user).vendor(VendorEnum.BMW).build()));
        return user;
    }

    private StartChargingRequest createStartRequest() {
        return StartChargingRequest.builder()
                .provider(ProviderEnum.JAPAN)
                .userId("testUserId")
                .vehicleId("testVehicleId")
                .vendor(VendorEnum.BMW)
                .build();
    }

    private StopChargingRequest createStopRequest() {
        return StopChargingRequest.builder()
                .sessionId("sessionId")
                .provider(ProviderEnum.JAPAN)
                .userId("testUserId")
                .vehicleId("testVehicleId")
                .build();
    }

    private SessionEntity createSession(UserEntity user) {
        return SessionEntity.builder().sessionId("sessionId")
                .provider(ProviderEnum.JAPAN)
                .user(user)
                .vehicle(user.getVehicles().get(0))
                .status(SessionStatus.ACTIVE).build();
    }
}