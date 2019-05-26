package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

/**
 * @author jhmarryme.cn
 * @date 2019/5/22 15:12
 */
@Data
public class SpuBo extends Spu {
    @Transient
    private String cname;
    @Transient
    private String bname;
    @Transient
    SpuDetail spuDetail;// 商品详情
    @Transient
    List<Sku> skus;// sku列表
}
