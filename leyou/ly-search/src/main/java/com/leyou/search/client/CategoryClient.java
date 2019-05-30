package com.leyou.search.client;

import com.leyou.item.api.CategoryApi;
import com.leyou.item.api.GoodsApi;
import com.leyou.item.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author jhmarryme.cn
 * @date 2019/5/23 16:37
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
