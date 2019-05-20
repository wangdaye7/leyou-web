package com.leyou.item.web;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecGroupParams;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroup(@PathVariable("cid") Long cid){
        return ResponseEntity.ok(specService.queryGroupById(cid));
    }

    @GetMapping("params/{gid}")
    public ResponseEntity<List<SpecGroupParams>> queryParams(@PathVariable("gid") Long gid){
        return ResponseEntity.ok(specService.queryParamByGid(gid));
    }
}
