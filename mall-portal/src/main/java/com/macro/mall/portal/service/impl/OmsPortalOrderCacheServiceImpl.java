package com.macro.mall.portal.service.impl;

import com.macro.mall.common.service.RedisService;
import com.macro.mall.model.OmsOrder;
import com.macro.mall.portal.service.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 前台订单管理缓存 Service 实现类。<br>
 * 【性能优化备注】这个类是为了优化性能而添加的
 *
 * @author Camio1945
 */
@Service
public class OmsPortalOrderCacheServiceImpl implements OmsPortalOrderCacheService {
  @Autowired private RedisService redisService;

  @Value("${redis.database}")
  private String REDIS_DATABASE;

  @Value("${redis.expire.common}")
  private Long REDIS_EXPIRE;

  @Value("${redis.key.preOrders}")
  private String REDIS_KEY_PRE_ORDERS;

  @Override
  public void putPreOrderList(List<OmsOrder> preOrders) {
    OmsOrder[] preOrderArray = new OmsOrder[preOrders.size()];
    preOrders.toArray(preOrderArray);
    redisService.sAdd(REDIS_DATABASE + ":" + REDIS_KEY_PRE_ORDERS, REDIS_EXPIRE, preOrderArray);
  }

  @Override
  public OmsOrder popPreOrder() {
    return (OmsOrder) redisService.sPop(REDIS_DATABASE + ":" + REDIS_KEY_PRE_ORDERS);
  }
}
