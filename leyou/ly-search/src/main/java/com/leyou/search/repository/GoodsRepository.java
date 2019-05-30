package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author jhmarryme.cn
 * @date 2019/5/26 18:37
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
