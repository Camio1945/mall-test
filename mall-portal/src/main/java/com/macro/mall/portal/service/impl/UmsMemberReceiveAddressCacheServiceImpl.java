package com.macro.mall.portal.service.impl;

import com.macro.mall.common.service.RedisService;
import com.macro.mall.model.UmsMemberReceiveAddress;
import com.macro.mall.portal.service.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 用户地址管理缓存接口。<br>
 * 【性能优化备注】这个类是为了优化性能而添加的
 *
 * @author Camio1945
 */
@Service
public class UmsMemberReceiveAddressCacheServiceImpl
    implements UmsMemberReceiveAddressCacheService {
  @Autowired private RedisService redisService;

  @Value("${redis.database}")
  private String REDIS_DATABASE;

  @Value("${redis.expire.common}")
  private Long REDIS_EXPIRE;

  @Value("${redis.key.memberAddresses}")
  private String REDIS_KEY_MEMBER_ADDRESSES;

  @SuppressWarnings("unchecked")
  @Override
  public List<UmsMemberReceiveAddress> getAddressListOfMember(Long memberId) {
    String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER_ADDRESSES + ":" + memberId;
    return (List<UmsMemberReceiveAddress>) redisService.get(key);
  }

  @Override
  public void setAddressListOfMember(Long memberId, List<UmsMemberReceiveAddress> addressList) {
    String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER_ADDRESSES + ":" + memberId;
    redisService.set(key, addressList, REDIS_EXPIRE);
  }

  @Override
  public void delAddressListOfMember(Long memberId) {
    String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER_ADDRESSES + ":" + memberId;
    redisService.del(key);
  }
}
