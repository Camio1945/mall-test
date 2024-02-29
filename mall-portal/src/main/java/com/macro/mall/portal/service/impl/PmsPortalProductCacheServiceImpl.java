package com.macro.mall.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.macro.mall.common.log.TrackExecutionTime;
import com.macro.mall.common.service.RedisService;
import com.macro.mall.portal.dao.PortalProductDao;
import com.macro.mall.portal.domain.*;
import com.macro.mall.portal.service.PmsPortalProductCacheService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 前台商品管理缓存Service实现类 Created by macro on 2020/4/6. <br>
 * // TODO 未完成：PromotionProduct 中的信息变化时，都应该删除缓存。但是由于 portal 依赖了 admin，但 admin 没有依赖 portal，而相关的信息修改又是在 admin 中进行的，所以这里的 delPromotionProduct 无法被直接调用到，需要使用消息队列的方式来发布订阅。由于在我的测试中并没有用到促销相关的信息，因此对测试的影响不大，所以这里暂时不做处理。
 */
@Service
public class PmsPortalProductCacheServiceImpl implements PmsPortalProductCacheService {
  @Autowired private RedisService redisService;
  @Autowired private PortalProductDao portalProductDao;

  @Value("${redis.database}")
  private String REDIS_DATABASE;

  @Value("${redis.expire.common}")
  private Long REDIS_EXPIRE;

  @Value("${redis.key.promotionProduct}")
  private String REDIS_KEY_PROMOTION_PRODUCT;

  @Override
  public List<PromotionProduct> getPromotionProductList(@NonNull List<Long> ids) {
    Assert.notEmpty(ids, "ids不能为空");
    List<String> keys =
        ids.stream()
            .map(id -> REDIS_DATABASE + ":" + REDIS_KEY_PROMOTION_PRODUCT + ":" + id)
            .collect(Collectors.toList());
    List<Object> objects = redisService.multiGet(keys);
    if (objects != null) {
      Assert.isTrue(objects.size() == ids.size(), "缓存条数与id数不一致");
      for (int i = 0; i < objects.size(); i++) {
        Object object = objects.get(i);
        if (object == null) {
          Long productId = ids.get(i);
          List<Long> singleIds = new ArrayList<>();
          singleIds.add(productId);
          List<PromotionProduct> promotionProductList =
              portalProductDao.getPromotionProductList(singleIds);
          if (CollUtil.isNotEmpty(promotionProductList)) {
            redisService.set(
                REDIS_DATABASE + ":" + REDIS_KEY_PROMOTION_PRODUCT + ":" + productId,
                promotionProductList.get(0),
                REDIS_EXPIRE);
            objects.set(i, promotionProductList.get(0));
          }
        }
      }
      return objects.stream().map(o -> (PromotionProduct) o).collect(Collectors.toList());
    }
    List<PromotionProduct> promotionProductList = portalProductDao.getPromotionProductList(ids);
    if (promotionProductList != null) {
      for (PromotionProduct promotionProduct : promotionProductList) {
        redisService.set(
            REDIS_DATABASE + ":" + REDIS_KEY_PROMOTION_PRODUCT + ":" + promotionProduct.getId(),
            promotionProduct,
            REDIS_EXPIRE);
      }
    }
    return promotionProductList;
  }

  @Override
  public void delPromotionProduct(@NonNull Long id) {
    redisService.del(REDIS_DATABASE + ":" + REDIS_KEY_PROMOTION_PRODUCT + ":" + id);
  }
}
