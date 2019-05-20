package com.leyou.item.web;

import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import com.leyou.item.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
 * @author jhmarryme.cn
 * @date 2019/5/17 13:08
 */

@MapperScan("com.leyou.item.mapper")
@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 根据页面传的信息查询品牌
     * @param page
     * @param rows
     * @param key
     * @param sortBy
     * @param desc
     * @return ResponseEntity<T>
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc
    ){
        return ResponseEntity.ok(brandService.queryBrandByPage(page, rows, key, sortBy, desc));
    }

    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("categories") List<Long> cids){
        brandService.saveBrand(brand, cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
