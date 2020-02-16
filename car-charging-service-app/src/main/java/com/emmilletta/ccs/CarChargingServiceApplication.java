package com.emmilletta.ccs;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.annotation.PostConstruct;

/**
 * That class is used to bootstrap and launch a Spring application.
 */
@SpringBootApplication
@EnableFeignClients
public class CarChargingServiceApplication {

//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @PostConstruct
//    public void setUp() {
//        objectMapper.findAndRegisterModules();
//        objectMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
//        objectMapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
//        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
//    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CarChargingServiceApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
