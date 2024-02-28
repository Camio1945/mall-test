package com.macro.mall.dao;

import com.macro.mall.model.PmsProductAttributeValue;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/** 商品属性值管理自定义Dao Created by macro on 2018/4/26. */
public interface PmsProductAttributeValueDao {
  /** 批量创建 */
  int insertList(@Param("list") List<PmsProductAttributeValue> productAttributeValueList);
}
