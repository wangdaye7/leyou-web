package com.leyou.item.api;

import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.vo.PageResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jhmarryme.cn
 * @date 2019/5/24 12:51
 */
public interface GoodsApi {
    /**
     * 通过条件查询用于界面显示的SpuBo
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("spu/page")
    PageResult<SpuBo> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key
    );

    /**
     * 保存商品信息
     * @param spuBo
     */
    @PostMapping("spu/goods")
    Void saveGoods(@RequestBody SpuBo spuBo);

    /**
     * 更新商品信息
     * @param spuBo
     * @return
     */
    @PutMapping("spu/goods")
    Void updateGoods(@RequestBody SpuBo spuBo);

    /**
     * 根据spuid查询商品detail
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{id}")
    SpuDetail queryDetailById(@PathVariable("id") Long spuId);
    /**
     * 根据spuid查询出其下所有sku信息
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id") Long spuId);
}
