package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecGroupParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author jhmarryme.cn
 * @date 2019/5/20 18:18
 */

@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    public List<SpecGroup> queryGroupById(Long cid) {

        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        //查询
        List<SpecGroup> groups = specGroupMapper.select(specGroup);
        if (CollectionUtils.isEmpty(groups)){
            //查询结果为空
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }

        return groups;
    }

    public List<SpecGroupParams> queryParamByGid(Long gid) {

        return null;
    }
}
