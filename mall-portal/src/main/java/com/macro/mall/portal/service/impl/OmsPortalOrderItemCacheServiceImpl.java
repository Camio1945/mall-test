package com.macro.mall.portal.service.impl;

import com.macro.mall.common.service.RedisService;
import com.macro.mall.model.OmsOrderItem;
import com.macro.mall.portal.service.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 前台订单商品缓存 Service 实现类。<br>
 * 【性能优化备注】这个类是为了优化性能而添加的
 *
 * @author Camio1945
 */
@Service
public class OmsPortalOrderItemCacheServiceImpl implements OmsPortalOrderItemCacheService {
  @Autowired private RedisService redisService;

  @Value("${redis.database}")
  private String REDIS_DATABASE;

  @Value("${redis.expire.common}")
  private Long REDIS_EXPIRE;

  @Value("${redis.key.preOrderItems}")
  private String REDIS_KEY_PRE_ORDER_ITEMS;

  @Override
  public void putPreOrderItemList(List<OmsOrderItem> preOrderItems) {
    OmsOrderItem[] preOrderItemArray = new OmsOrderItem[preOrderItems.size()];
    preOrderItems.toArray(preOrderItemArray);
    redisService.sAdd(
        REDIS_DATABASE + ":" + REDIS_KEY_PRE_ORDER_ITEMS, REDIS_EXPIRE, preOrderItemArray);
  }

  @Override
  public OmsOrderItem popPreOrderItem() {
    return (OmsOrderItem) redisService.sPop(REDIS_DATABASE + ":" + REDIS_KEY_PRE_ORDER_ITEMS);
  }
}
