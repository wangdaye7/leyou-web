package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author jhmarryme.cn
 * @date 2019/5/15 15:14
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionEnum {

    //价格不能为空
    PRICE_CANNOT_BE_NULL(400, "价格不能为空!"),
    CATEGORY_NOT_FOUND(404, "商品分类未找到!"),
    BRAND_NOT_FOUND(404, "商品品牌未找到!"),
    BRAND_SAVE_ERROR(500, "新增品牌失败!"),
    UPLOAD_FILE_ERROR(500, "文件上传失败" ),
    FILE_TYPE_ERROR(500, "文件类型错误" ),
    SPEC_GROUP_NOT_FOUND(404, "商品规格组没查到" ),
    SPEC_GROUP_PARAM_NOT_FOUND(404, "商品规格组参数没查到"),
    GOODS_NOT_FOUND(404, "商品没查到"),
    GOODS_SAVE_ERROR(500, "商品保存失败");
    private int code;
    private String message;
}
