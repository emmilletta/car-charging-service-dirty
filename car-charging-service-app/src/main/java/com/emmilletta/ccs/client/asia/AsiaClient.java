package com.emmilletta.ccs.client.asia;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Alla Danko
 */
@FeignClient(name = "asia-client", url = "${ccs.client.asia.base-url}",
        configuration = AsiaClientConfiguration.class)
public interface AsiaClient extends Asia {
}
