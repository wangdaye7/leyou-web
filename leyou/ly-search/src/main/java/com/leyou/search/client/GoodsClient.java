package com.leyou.search.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author jhmarryme.cn
 * @date 2019/5/26 16:55
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
