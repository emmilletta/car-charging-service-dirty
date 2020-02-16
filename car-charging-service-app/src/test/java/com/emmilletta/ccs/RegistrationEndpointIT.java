package com.emmilletta.ccs;

import com.emmilletta.ccs.dto.DeregistrationUserRequest;
import com.emmilletta.ccs.dto.GetUserInfoRequest;
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

import java.time.LocalDate;
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
class RegistrationEndpointIT {

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
    void registrationNullProvider() throws Exception {
        RegistrationUserRequest request = RegistrationUserRequest.builder()
                .provider(null)
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId").vendor(VendorEnum.BMW).build())
                .email("testUserEmail@gmail.com")
                .build();
        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/register")
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
    void registrationNullEmail() throws Exception {
        RegistrationUserRequest request = RegistrationUserRequest.builder()
                .provider(ProviderEnum.ASIA)
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId").vendor(VendorEnum.BMW).build())
                .email(null)
                .build();
        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/register")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.MISSING_PARAMETER, content.getResponseCode());
        Assert.assertTrue(content.getMessage().contains("email"));
    }

    @Test
    void registrationWrongEmail() throws Exception {
        RegistrationUserRequest request = RegistrationUserRequest.builder()
                .provider(ProviderEnum.ASIA)
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId").vendor(VendorEnum.BMW).build())
                .email("testUserEmail")
                .build();
        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/register")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.MISSING_PARAMETER, content.getResponseCode());
        Assert.assertTrue(content.getMessage().contains("email"));
    }

    @Test
    void registrationNullVehicleId() throws Exception {
        RegistrationUserRequest request = RegistrationUserRequest.builder()
                .provider(ProviderEnum.ASIA)
                .vehicle(RegistrationVehicle.builder().vehicleId(null).vendor(VendorEnum.BMW).build())
                .email("testUserEmail@gmail.com")
                .build();
        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/register")
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
    void registrationWithoutVehicles() throws Exception {
        RegistrationUserRequest request = RegistrationUserRequest.builder()
                .provider(ProviderEnum.ASIA)
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
    void registrationUpdateUser() throws Exception {
        UserEntity user = createUser();
        userRepository.save(user);

        RegistrationUserRequest request = RegistrationUserRequest.builder()
                .provider(ProviderEnum.ASIA)
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId").vendor(VendorEnum.BMW).build())
                .email("testUserEmail2@gmail.com")
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
        Assert.assertEquals("testUserEmail2@gmail.com",
                userRepository.findByActiveUserId(content.getData().getUserId()).get().getEmail());
    }

    @Test
    void registrationUpdateVehicle() throws Exception {
        UserEntity user = createUser();
        userRepository.save(user);

        RegistrationUserRequest request = RegistrationUserRequest.builder()
                .provider(ProviderEnum.ASIA)
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId").vendor(VendorEnum.MERCEDES).build())
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
        Assert.assertEquals(1,
                userRepository.findByActiveUserId(content.getData().getUserId()).get().getVehicles().size());
        Assert.assertEquals(VendorEnum.MERCEDES,
                userRepository.findByActiveUserId(content.getData().getUserId()).get().getVehicles().get(0).getVendor());
    }

    @Test
    void registrationAddVehicle() throws Exception {
        UserEntity user = createUser();
        userRepository.save(user);

        RegistrationUserRequest request = RegistrationUserRequest.builder()
                .provider(ProviderEnum.ASIA)
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId2").vendor(VendorEnum.BMW).build())
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
        Assert.assertEquals(2,
                userRepository.findByActiveUserId(content.getData().getUserId()).get().getVehicles().size());
    }

    @Test
    void deregistrationNullProvider() throws Exception {
        DeregistrationUserRequest request = DeregistrationUserRequest.builder()
                .provider(null)
                .userId("testUserId")
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId").vendor(VendorEnum.BMW).build())
                .build();
        MvcResult result = mvc.perform(put(CcsApi.apiPath + "/deregister")
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
    void deregistrationNullUserId() throws Exception {
        DeregistrationUserRequest request = DeregistrationUserRequest.builder()
                .provider(ProviderEnum.ASIA)
                .userId(null)
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId").vendor(VendorEnum.BMW).build())
                .build();
        MvcResult result = mvc.perform(put(CcsApi.apiPath + "/deregister")
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
    void deregistrationNullVehicleId() throws Exception {
        DeregistrationUserRequest request = DeregistrationUserRequest.builder()
                .provider(ProviderEnum.ASIA)
                .userId("testUserId")
                .vehicle(RegistrationVehicle.builder().vehicleId(null).vendor(VendorEnum.BMW).build())
                .build();
        MvcResult result = mvc.perform(put(CcsApi.apiPath + "/deregister")
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
    void deregistrationUserWithoutVehicles() throws Exception {
        UserEntity user = createUser();
        userRepository.save(user);

        DeregistrationUserRequest request = DeregistrationUserRequest.builder()
                .provider(ProviderEnum.ASIA)
                .userId("testUserId")
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId").vendor(VendorEnum.BMW).build())
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
    void deregistrationUserWithVehicles() throws Exception {
        UserEntity user = createUser();
        userRepository.save(user);

        DeregistrationUserRequest request = DeregistrationUserRequest.builder()
                .provider(ProviderEnum.ASIA)
                .userId("testUserId")
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId").vendor(VendorEnum.BMW).build())
                .build();
        MvcResult result = mvc.perform(put(CcsApi.apiPath + "/deregister")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.USER_DEREGISTERED, content.getResponseCode());
    }

    @Test
    void getUserInfoSuccessful() throws Exception {
        GetUserInfoRequest request = GetUserInfoRequest.builder()
                .userId("testUserId")
                .status(UserStatus.ACTIVE)
                .startDate(LocalDate.now())
                .stopDate(LocalDate.now())
                .build();
        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/getUserInfo")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.USER_INFO_RETURNED, content.getResponseCode());
    }

    @Test
    void getUserInfoNullUserId() throws Exception {
        GetUserInfoRequest request = GetUserInfoRequest.builder()
                .userId("testUserId")
                .status(UserStatus.ACTIVE)
                .startDate(LocalDate.now())
                .stopDate(LocalDate.now())
                .build();
        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/getUserInfo")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.USER_INFO_RETURNED, content.getResponseCode());
    }

    @Test
    void getUserInfoNoUserFound() throws Exception {
        UserEntity user = createUser();
        user.getVehicles().get(0).setVehicleId("wrong_access_token");
        userRepository.save(user);

        GetUserInfoRequest request = GetUserInfoRequest.builder()
                .userId("testUserId")
                .status(UserStatus.ACTIVE)
                .startDate(LocalDate.now())
                .stopDate(LocalDate.now())
                .build();
        MvcResult result = mvc.perform(post(CcsApi.apiPath + "/getUserInfo")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CcsResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), CcsResponse.class);
        Assert.assertEquals(CcsResponseStatus.USER_INFO_RETURNED, content.getResponseCode());
    }

    private UserEntity createUser() {
        UserEntity user = UserEntity.builder().userId("testUserId").email("testUserEmail@gmail.com")
                .registrationDate(ZonedDateTime.now()).status(UserStatus.ACTIVE).build();
        user.setVehicles(Arrays.asList(VehicleEntity.builder().vehicleId("testVehicleId").user(user).vendor(VendorEnum.BMW).build()));
        return user;
    }

    private DeregistrationUserRequest createDeregistrationRequest() {
        return DeregistrationUserRequest.builder()
                .provider(ProviderEnum.ASIA)
                .userId("testUserId")
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId").vendor(VendorEnum.BMW).build())
                .build();
    }

    private RegistrationUserRequest createRegistrationRequest() {
        return RegistrationUserRequest.builder()
                .provider(ProviderEnum.ASIA)
                .vehicle(RegistrationVehicle.builder().vehicleId("testVehicleId").vendor(VendorEnum.BMW).build())
                .email("testUserEmail@gmail.com")
                .build();
    }
}