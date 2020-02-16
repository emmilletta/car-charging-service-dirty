package com.emmilletta.ccs.client.europe;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Alla Danko
 */
@FeignClient(name = "europe-client", url = "${ccs.client.europe.base-url}",
        configuration = EuropeClientConfiguration.class)
public interface EuropeClient extends Europe {
}
