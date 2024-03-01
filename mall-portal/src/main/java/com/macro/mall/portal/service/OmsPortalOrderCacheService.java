package com.macro.mall.portal.service;

import com.macro.mall.model.OmsOrder;
import java.util.List;

/**
 * 前台订单管理缓存 Service
 *
 * @author Camio1945
 */
public interface OmsPortalOrderCacheService {

  /**
   * 把预订单信息存入缓存
   *
   * @param preOrders 预订单列表
   */
  void putPreOrderList(List<OmsOrder> preOrders);

  /**
   * 从缓存中获取预一个订单信息（由 Redis 保证每次获取的都不一样）
   *
   * @return 预订单
   */
  OmsOrder popPreOrder();
}
