package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * IdListMapper, 通过主键数组查询, 返回数组
 * @author jhmarryme.cn
 * @date 2019/5/16 12:35
 */
public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category, Long> {
}
