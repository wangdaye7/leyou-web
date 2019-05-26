package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailsMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.*;
import com.leyou.item.vo.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jhmarryme.cn
 * @date 2019/5/22 15:22
 */

@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SpuDetailsMapper detailsMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private SkuMapper skuMapper;
    public PageResult<SpuBo> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        //控制每页大小
        PageHelper.startPage(page, Math.min(rows, 200));
        //创建查询对象
        Example example = new Example(Spu.class);
        //创建查询条件
        Example.Criteria criteria = example.createCriteria();

        //是否下架
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        //过滤关键字
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }

        //查询
        //这里需要注意 将查询到的结果集强转为page对象, 为了取出查询结果的总条数
        Page<Spu> spus = (Page<Spu>) spuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(spus)) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        //处理分类名和品牌名
        List<SpuBo> spuBos = spus.stream().map(spu -> {
            //转换对象
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            //处理分类名
            List<String> cnames = categoryService.queryByIds(Arrays.asList(spuBo.getCid1(), spuBo.getCid2(), spuBo.getCid3()))
                    .stream().map(cname -> cname.getName()).collect(Collectors.toList());
            spuBo.setCname(StringUtils.join(cnames, "/"));
            //处理品牌名
            spuBo.setBname(brandService.queryById(spu.getBrandId()).getName());

            //返回经过转换并处理的spubo
            return spuBo;
        }).collect(Collectors.toList());

        return new PageResult<>(spus.getTotal(), spuBos);
    }

    @Transactional
    public void saveGoods(SpuBo spuBo) {

        //新增spu
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(false);
        int count = spuMapper.insert(spuBo);
        if (count != 1) {
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
        //新增spu_detail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        detailsMapper.insert(spuDetail);
        //新增sku
        List<Sku> skus = spuBo.getSkus();
        for (Sku sku : skus) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spuBo.getId());

            count = skuMapper.insert(sku);
            if (count != 1){
                throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
            }
            //新增stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            count = stockMapper.insert(stock);
            if (count != 1){
                throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
            }
        }

    }
}
