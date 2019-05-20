package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecGroupParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecGroupParam;
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

    private final SpecGroupMapper specGroupMapper;
    private final SpecGroupParamMapper specGroupParamMapper;
    public SpecificationService(SpecGroupMapper specGroupMapper, SpecGroupParamMapper specGroupParamMapper) {
        this.specGroupMapper = specGroupMapper;
        this.specGroupParamMapper = specGroupParamMapper;
    }

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

    public List<SpecGroupParam> queryParamByGid(Long gid) {

        //创建查询对象
        SpecGroupParam params = new SpecGroupParam();
        params.setGroupId(gid);

        //查询
        List<SpecGroupParam> list = specGroupParamMapper.select(params);
        //判断结果
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.SPEC_GROUP_PARAM_NOT_FOUND);
        }
        return list;
    }
}
