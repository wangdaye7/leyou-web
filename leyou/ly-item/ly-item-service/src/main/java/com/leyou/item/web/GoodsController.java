package com.leyou.item.web;

import com.leyou.item.pojo.SpuBo;
import com.leyou.item.service.GoodsService;
import com.leyou.item.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jhmarryme.cn
 * @date 2019/5/22 15:16
 */

@RestController
@RequestMapping("spu")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key
    ){
        PageResult<SpuBo> spuBoPageResult = goodsService.querySpuByPage(page, rows, saleable, key);
        return ResponseEntity.ok(spuBoPageResult);
    }
}
