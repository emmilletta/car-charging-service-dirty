package com.emmilletta.ccs;

import com.emmilletta.ccs.dto.DeregistrationUserRequest;
import com.emmilletta.ccs.dto.RegistrationResponse;
import com.emmilletta.ccs.dto.RegistrationUserRequest;
import com.emmilletta.ccs.dto.RegistrationVehicle;
import com.emmilletta.ccs.dto.CcsResponse;
import com.emmilletta.ccs.dto.CcsResponseStatus;
import com.emmilletta.ccs.dto.ProviderEnum;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Alla Danko
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RegistrationJapanProviderIT {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test
    void registrationSuccessful() throws Exception {
        RegistrationUserRequest request = RegistrationUserRequest.builder()
                .provider(ProviderEnum.JAPAN)
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId").vendor(VendorEnum.BMW).build())
                .email("testUserEmail@gmail.com")
                .build();
        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/register")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CcsResponse<RegistrationResponse> content = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<CcsResponse<RegistrationResponse>>() {});
        Assert.assertEquals(CcsResponseStatus.USER_REGISTERED, content.getResponseCode());
        Assert.assertTrue(userRepository.findByActiveUserId(content.getData().getUserId()).isPresent());
    }


    @Test
    void registrationWrongCarType() throws Exception {
        RegistrationUserRequest request = RegistrationUserRequest.builder()
                .provider(ProviderEnum.JAPAN)
                .vehicle(RegistrationVehicle.builder().vehicleId("wrong_car_type").vendor(VendorEnum.BMW).build())
                .email("testUserEmail@gmail.com")
                .build();
        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/register")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.WRONG_CAR_TYPE, content.getResponseCode());
        Assert.assertTrue(userRepository.findAll().isEmpty());
    }

    @Test
    void deregistrationSuccessful() throws Exception {
        UserEntity user = createUser();
        userRepository.save(user);

        DeregistrationUserRequest request = DeregistrationUserRequest.builder()
                .provider(ProviderEnum.JAPAN)
                .userId("testUserId")
                .build();
        MvcResult result = mvc.perform(put(CcsApi.apiPath + "/deregister")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.USER_DEREGISTERED, content.getResponseCode());
        Assert.assertEquals(UserStatus.INACTIVE, userRepository.findByUserId("testUserId").get().getStatus());
    }


    @Test
    void deregistrationWrongCarType() throws Exception {
        UserEntity user = createUser();
        user.getVehicles().get(0).setVehicleId("wrong_car_type");
        userRepository.save(user);

        DeregistrationUserRequest request = DeregistrationUserRequest.builder()
                .provider(ProviderEnum.JAPAN)
                .userId("testUserId")
                .vehicle(RegistrationVehicle.builder().vehicleId("wrong_car_type").vendor(VendorEnum.BMW).build())
                .build();
        MvcResult result = mvc.perform(put(CcsApi.apiPath + "/deregister")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.WRONG_CAR_TYPE, content.getResponseCode());
        Assert.assertTrue(userRepository.findByActiveUserId("testUserId").isPresent());
    }

    @Test
    void deregistratioVehicleNotFound() throws Exception {
        UserEntity user = createUser();
        user.getVehicles().get(0).setVehicleId("vehicle_not_found");
        userRepository.save(user);

        DeregistrationUserRequest request = DeregistrationUserRequest.builder()
                .provider(ProviderEnum.JAPAN)
                .userId("testUserId")
                .vehicle(RegistrationVehicle.builder().vehicleId("vehicle_not_found").vendor(VendorEnum.BMW).build())
                .build();
        MvcResult result = mvc.perform(put(CcsApi.apiPath + "/deregister")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.VEHICLE_NOT_FOUND, content.getResponseCode());
        Assert.assertTrue(userRepository.findByActiveUserId("testUserId").isPresent());
    }

    private UserEntity createUser() {
        UserEntity user = UserEntity.builder().userId("testUserId").userAccessKey("testUserAccessKey")
                .email("testUserEmail1@gmail.com").registrationDate(ZonedDateTime.now()).status(UserStatus.ACTIVE).build();
        user.setVehicles(Arrays.asList(VehicleEntity.builder().vehicleId("testVehicleId").user(user).vendor(VendorEnum.BMW).build()));
        return user;
    }

    private DeregistrationUserRequest createDeregistrationRequest() {
        return DeregistrationUserRequest.builder()
                .provider(ProviderEnum.JAPAN)
                .userId("testUserId")
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId").vendor(VendorEnum.BMW).build())
                .build();
    }

    private RegistrationUserRequest createRegistrationRequest() {
        return RegistrationUserRequest.builder()
                .provider(ProviderEnum.JAPAN)
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId").vendor(VendorEnum.BMW).build())
                .email("testUserEmail@gmail.com")
                .build();
    }
}