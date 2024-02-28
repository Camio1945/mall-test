package com.macro.mall.dao;

import com.macro.mall.model.SmsCouponProductCategoryRelation;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/** 优惠券和商品分类关系管理自定义Dao Created by macro on 2018/8/28. */
public interface SmsCouponProductCategoryRelationDao {
  /** 批量创建 */
  int insertList(@Param("list") List<SmsCouponProductCategoryRelation> productCategoryRelationList);
}
