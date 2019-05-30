package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author jhmarryme.cn
 * @date 2019/5/26 18:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {

    @Autowired
    private ElasticsearchTemplate template;

    @Test
    public void creatIndex() {
        //创建索引库
        template.createIndex(Goods.class);
        //配置映射
        template.putMapping(Goods.class);
    }
}