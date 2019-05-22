package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.vo.PageResult;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author jhmarryme.cn
 * @date 2019/5/17 13:08
 */

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String key, String sortBy, Boolean desc) {
        //分页
        PageHelper.startPage(page, rows);

        //创建一个查询对象, 需要传入字节码
        Example example = new Example(Brand.class);
        //过滤
        if (StringUtils.isNotBlank(key)){
            // 创建查询条件
            example.createCriteria().orLike("name", "%"+key+"%").
                    orEqualTo("letter", key.toUpperCase()); //表中首字母为大写, 将用户输入相应转换
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + (desc ? " DESC" : " ASC")); //拼接字符串时需要注意加上空格
        }
        //查询
        List<Brand> brands = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(brands)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //将查询结果封装到PageInfo中, 以便获取分页数据
        PageInfo<Brand> brandPageInfo = new PageInfo<>(brands);
        //返回PageResult
        return new PageResult<>(brandPageInfo.getTotal(), brands);
    }

    public void saveBrand(Brand brand, List<Long> cids) {

        //1. 保存品牌
        //主键自动生成, 确保传入的为null
        brand.setId(null);
        //mapper的insertSelective可以保存有空字段的对象
        int count = brandMapper.insertSelective(brand);
        if (count != 1) {
            throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
        }

        //2. 将品牌ID和分类ID存入中间表
        for (Long cid : cids) {
            count = brandMapper.insertCategoryAndBrand(brand.getId(), cid);
            if (count != 1) {
                throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
            }
        }
    }

    public Brand queryById(Long brandId) {
        Brand brand = brandMapper.selectByPrimaryKey(brandId);
        if (brand == null) {
            throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
        }
        return brand;
    }
}
