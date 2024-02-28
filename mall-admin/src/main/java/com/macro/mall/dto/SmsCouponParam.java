package com.macro.mall.dto;

import com.macro.mall.model.SmsCoupon;
import com.macro.mall.model.SmsCouponProductCategoryRelation;
import com.macro.mall.model.SmsCouponProductRelation;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** 优惠券信息封装，包括绑定商品和分类 Created by macro on 2018/8/28. */
public class SmsCouponParam extends SmsCoupon {
  @Getter
  @Setter
  @ApiModelProperty("优惠券绑定的商品")
  private List<SmsCouponProductRelation> productRelationList;

  @Getter
  @Setter
  @ApiModelProperty("优惠券绑定的商品分类")
  private List<SmsCouponProductCategoryRelation> productCategoryRelationList;
}
