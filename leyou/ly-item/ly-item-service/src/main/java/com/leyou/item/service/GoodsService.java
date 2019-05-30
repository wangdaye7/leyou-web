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

import java.util.*;
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

   /* public PageResult<SpuBo> testQuerySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        PageHelper.startPage(page, Math.min(200, rows));
        //创建查询构建器
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (saleable != null){
            criteria.orEqualTo(saleable);
        }

        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        List<Spu> spuList = spuMapper.selectByExample(example);
        PageInfo<Spu> spuPageInfo = new PageInfo<>(spuList);

        List<SpuBo> spuBoList = spuList.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);

            List<String> cnames = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid1(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spuBo.setCname(StringUtils.join(cnames, '/'));
            spuBo.setBname(brandService.queryById(spu.getBrandId()).getName());

            return spuBo;
        }).collect(Collectors.toList());

        return new PageResult<>(spuPageInfo.getTotal(), spuBoList);
    }*/




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
        //保存sku和stock
        saveSkuAndStock(spuBo);
    }

    private void saveSkuAndStock(SpuBo spuBo) {
        //新增sku
        int count;
        List<Sku> skus = spuBo.getSkus();
        List<Stock> stocks = new ArrayList<>();
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
            //添加stock, 等待批处理
            stocks.add(stock);
        }
        //批量新增库存
        count = stockMapper.insertList(stocks);
        if (count != stocks.size()) {
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
    }


    public void testSaveGoods(SpuBo spuBo){

        //保存spu
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(false);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        int spuCount = spuMapper.insert(spuBo);
        //判断是否保存成功



        //保存spudetails
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        int detailCount = detailsMapper.insert(spuDetail);
        //保存sku
        List<Sku> skuList = spuBo.getSkus();
        List<Stock> stockList = new ArrayList<>();
        for (Sku sku : skuList) {
            sku.setSpuId(spuBo.getId());
            sku.setId(null);
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());

            int skuInsert = skuMapper.insert(sku);
            //保存库存信息
            Stock stock = new Stock();
            stock.setStock(sku.getStock());
            stock.setSkuId(sku.getId());
            stockList.add(stock);
        }
        int stockInsert = stockMapper.insertList(stockList);
        if (stockInsert != stockList.size()) {
            //抛出异常
        }
    }


    @Transactional
    public void updateGoods(SpuBo spuBo) {
        //查询sku是否存在
        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());
        List<Sku> skuList = skuMapper.select(sku);
        //sku存在, 删除
        if (!CollectionUtils.isEmpty(skuList)) {
            //删除该商品所有的sku
            skuMapper.delete(sku);
            //删除库存信息
            List<Long> skuIds = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            stockMapper.deleteByIdList(skuIds);
        }
        //修改spu及spuDetail
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        spuBo.setLastUpdateTime(new Date());
        spuBo.setCreateTime(null);
        detailsMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
        int count = spuMapper.updateByPrimaryKeySelective(spuBo);
        if (count != 1){
            throw new LyException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
        count = detailsMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
        if (count != 1){
            throw new LyException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }

        //新增sku和stock
        saveSkuAndStock(spuBo);
    }

    public SpuDetail queryDetailById(Long spuId) {
        //查询spuDetail信息
        SpuDetail spuDetail = detailsMapper.selectByPrimaryKey(spuId);
        if (spuDetail == null){
            throw new LyException(ExceptionEnum.GOODS_DETAIL_NOT_FOUND);
        }
        return spuDetail;
    }


    public List<Sku> querySkuBySpuId(Long spuId) {
        //查询sku信息
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());

        //查询库存信息
        List<Stock> stockList = stockMapper.selectByIdList(ids);

        //先将stock信息抽取成map
        Map<Long, Integer> stockMap = stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        //将库存信息存入sku
        skuList.forEach(s -> s.setStock(stockMap.get(s.getId())));

        return skuList;
    }
}
