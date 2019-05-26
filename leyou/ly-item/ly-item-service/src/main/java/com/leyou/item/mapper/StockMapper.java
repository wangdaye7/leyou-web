package com.leyou.item.mapper;

import com.leyou.item.pojo.Stock;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author jhmarryme.cn
 * @date 2019/5/24 16:20
 */
public interface StockMapper extends IdListMapper<Stock, Long>, InsertListMapper<Stock> {
}
