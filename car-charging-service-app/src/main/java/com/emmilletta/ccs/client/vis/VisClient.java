package com.emmilletta.ccs.client.vis;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Alla Danko
 */
@FeignClient(name = "vis-client", url = "${ccs.client.vis.base-url}",
        configuration = VisClientConfiguration.class)
public interface VisClient extends Vis {
}
