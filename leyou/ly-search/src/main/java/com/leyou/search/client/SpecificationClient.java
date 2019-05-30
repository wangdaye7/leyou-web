package com.leyou.search.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author jhmarryme.cn
 * @date 2019/5/26 17:01
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
