package com.macro.mall.dao;

import com.macro.mall.model.PmsProductCategoryAttributeRelation;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/** 商品分类和属性关系自定义Dao Created by macro on 2018/5/23. */
public interface PmsProductCategoryAttributeRelationDao {
  /** 批量创建 */
  int insertList(
      @Param("list")
          List<PmsProductCategoryAttributeRelation> productCategoryAttributeRelationList);
}
