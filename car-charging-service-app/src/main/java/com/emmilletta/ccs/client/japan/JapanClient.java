package com.emmilletta.ccs.client.japan;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Alla Danko
 */
@FeignClient(name = "japan-client", url = "${ccs.client.japan.base-url}",
        configuration = JapanClientConfiguration.class)
public interface JapanClient extends Japan {
}
