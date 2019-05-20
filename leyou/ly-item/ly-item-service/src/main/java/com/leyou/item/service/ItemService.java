package com.leyou.item.service;

import com.leyou.item.pojo.Item;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author jhmarryme.cn
 * @date 2019/5/15 14:14
 */

@Service
public class ItemService {
    public Item saveItem(Item item){
        //新增商品
        int id = new Random().nextInt(100);
        item.setId(id);
        return item;
    }
}
