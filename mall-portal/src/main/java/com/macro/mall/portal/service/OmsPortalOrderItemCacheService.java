package com.macro.mall.portal.service;

import com.macro.mall.model.OmsOrderItem;
import java.util.List;

/**
 * 前台订单商品缓存 Service
 *
 * @author Camio1945
 */
public interface OmsPortalOrderItemCacheService {

  /**
   * 把预订单商品信息存入缓存
   *
   * @param preOrderItems 预订单商品列表
   */
  void putPreOrderItemList(List<OmsOrderItem> preOrderItems);

  /**
   * 从缓存中获取预一个订单信息（由 Redis 保证每次获取的都不一样）
   *
   * @return 预订单商品
   */
  OmsOrderItem popPreOrderItem();
}
