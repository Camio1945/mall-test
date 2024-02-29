package com.macro.mall.portal.service;

import com.macro.mall.portal.domain.*;
import java.util.List;

/** 前台商品管理缓存Service Created by macro on 2020/4/6. */
public interface PmsPortalProductCacheService {

  /**
   * 获取促销商品信息列表（缓存）
   *
   * @param ids 商品id列表
   * @return 促销商品信息列表
   */
  List<PromotionProduct> getPromotionProductList(List<Long> ids);

  /**
   * 删除促销商品信息（缓存）
   * @param id 商品id
   */
  void delPromotionProduct(Long id);
}
