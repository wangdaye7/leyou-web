package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jhmarryme.cn
 * @date 2019/5/30 16:36
 */

@Service
public class IndexService {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specClient;

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    public Goods buildGoods(Spu spu){

        Long spuId = spu.getId();
        Goods goods = new Goods();
        //查询分类
        List<Category> categoryList = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<String> cNames = categoryList.stream().map(Category::getName).collect(Collectors.toList());
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        
        String all = spu.getTitle() + StringUtils.join(cNames, " ") + brand.getName();
        //查询sku信息
        List<Sku> skuList = goodsClient.querySkuBySpuId(spuId);

        System.out.println("*****************************\n" + spu.getId());

        //查询商品详情
        SpuDetail spuDetail = goodsClient.queryDetailById(spuId);


        List<Map<String, Object>> skus = new ArrayList<>();
        List<Long> priceList = new ArrayList<>();
        //处理skus
        for (Sku sku : skuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("image", StringUtils.substringBefore(sku.getImages(), ","));
            map.put("price", sku.getPrice());
            skus.add(map);
            priceList.add(sku.getPrice());
        }

        //处理规格参数
        List<SpecGroupParam> params = specClient.queryParams(null, spu.getCid3(), true, null);

        //拿到商品的所有参数值
        String genericSpec = spuDetail.getGenericSpec();
        String specialSpec = spuDetail.getSpecialSpec();

        //将规格参数转换为map
        Map<Long, String> geneMap = JsonUtils.toMap(spuDetail.getGenericSpec(), Long.class, String.class);
        Map<Long, List<String>> specMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(),
                new TypeReference<Map<Long, List<String>>>() {
        });

        Map<String, Object> specs = new HashMap<>();

        for (SpecGroupParam param : params) {
            Object value = "";
            //判断通用/特有属性
            if (param.getGeneric()){
                value = geneMap.get(param.getId());
                if (param.getNumeric()){
                    if (value == null){
                        continue;
                    }
                    //处理成段
                    value = chooseSegment(value.toString(), param);
                }
            }else {
                value = specMap.get(param.getId());
            }
            //存入map
            specs.put(param.getName(), value);
        }


        //
        goods.setId(spu.getId());
        goods.setAll(all); //处理所有的搜索信息
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setPrice(priceList);// 处理价格
        goods.setSkus(JsonUtils.toString(skus)); //TODO sku信息
        goods.setSpecs(specs); // 规格参数

        return goods;
    }


    private String chooseSegment(String value, SpecGroupParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
