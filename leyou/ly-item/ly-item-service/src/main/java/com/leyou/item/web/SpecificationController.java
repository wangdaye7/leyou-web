package com.leyou.item.web;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecGroupParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jhmarryme.cn
 * @date 2019/5/20 18:19
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specService;

    /**
     * 根据分类id查询规格组
     * @param cid 分类id
     * @return 商品规格组数组
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroup(@PathVariable("cid") Long cid){
        return ResponseEntity.ok(specService.queryGroupById(cid));
    }

    /**
     * 根据分组id查询规格组参数
     * @param gid 组id0
     * @return 商品规格参数数组
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecGroupParam>> queryParams(@RequestParam("gid") Long gid){
        return ResponseEntity.ok(specService.queryParamByGid(gid));
    }
}
