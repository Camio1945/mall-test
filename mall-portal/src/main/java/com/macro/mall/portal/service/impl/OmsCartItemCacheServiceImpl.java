package com.macro.mall.portal.service.impl;

import com.macro.mall.common.log.TrackExecutionTime;
import com.macro.mall.common.service.RedisService;
import com.macro.mall.model.OmsCartItem;
import com.macro.mall.portal.service.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 购物车管理缓存业务实现类。<br>
 * 【性能优化备注】这个类是为了优化性能而添加的
 *
 * @author Camio1945
 */
@Service
public class OmsCartItemCacheServiceImpl implements OmsCartItemCacheService {
  @Autowired private RedisService redisService;

  @Value("${redis.database}")
  private String REDIS_DATABASE;

  @Value("${redis.expire.common}")
  private Long REDIS_EXPIRE;

  @Value("${redis.key.memberCartItems}")
  private String REDIS_KEY_MEMBER_CART_ITEMS;

  @SuppressWarnings("unchecked")
  @Override
  public List<OmsCartItem> getCartItemListOfMember(Long memberId) {
    String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER_CART_ITEMS + ":" + memberId;
    return (List<OmsCartItem>) redisService.get(key);
  }

  @Override
  public void setCartItemListOfMember(Long memberId, List<OmsCartItem> cartItemList) {
    String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER_CART_ITEMS + ":" + memberId;
    redisService.set(key, cartItemList, REDIS_EXPIRE);
  }

  @Override
  public void delCartItemListOfMember(Long memberId) {
    String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER_CART_ITEMS + ":" + memberId;
    redisService.del(key);
  }
}
