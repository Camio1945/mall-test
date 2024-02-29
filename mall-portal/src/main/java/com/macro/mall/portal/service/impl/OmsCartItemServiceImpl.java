package com.macro.mall.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.macro.mall.common.log.TrackExecutionTime;
import com.macro.mall.mapper.OmsCartItemMapper;
import com.macro.mall.model.OmsCartItem;
import com.macro.mall.model.OmsCartItemExample;
import com.macro.mall.model.UmsMember;
import com.macro.mall.portal.dao.PortalProductDao;
import com.macro.mall.portal.domain.CartProduct;
import com.macro.mall.portal.domain.CartPromotionItem;
import com.macro.mall.portal.service.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/** 购物车管理Service实现类 Created by macro on 2018/8/2. */
@Service
public class OmsCartItemServiceImpl implements OmsCartItemService {
  @Autowired private OmsCartItemMapper cartItemMapper;
  @Autowired private PortalProductDao productDao;
  @Autowired private OmsPromotionService promotionService;
  @Autowired private UmsMemberService memberService;
  @Autowired private OmsCartItemCacheService cartItemCacheService;

  @Override
  public int add(OmsCartItem cartItem) {
    int count;
    UmsMember currentMember = memberService.getCurrentMember();
    cartItem.setMemberId(currentMember.getId());
    cartItem.setMemberNickname(currentMember.getNickname());
    cartItem.setDeleteStatus(0);
    OmsCartItem existCartItem = getCartItem(cartItem);
    if (existCartItem == null) {
      cartItem.setCreateDate(new Date());
      count = cartItemMapper.insert(cartItem);
    } else {
      cartItem.setModifyDate(new Date());
      existCartItem.setQuantity(existCartItem.getQuantity() + cartItem.getQuantity());
      count = cartItemMapper.updateByPrimaryKey(existCartItem);
    }
    if (count > 0) {
      cartItemCacheService.delCartItemListOfMember(currentMember.getId());
    }
    return count;
  }

  /** 根据会员id,商品id和规格获取购物车中商品 */
  private OmsCartItem getCartItem(OmsCartItem cartItem) {
    OmsCartItemExample example = new OmsCartItemExample();
    OmsCartItemExample.Criteria criteria =
        example
            .createCriteria()
            .andMemberIdEqualTo(cartItem.getMemberId())
            .andProductIdEqualTo(cartItem.getProductId())
            .andDeleteStatusEqualTo(0);
    if (cartItem.getProductSkuId() != null) {
      criteria.andProductSkuIdEqualTo(cartItem.getProductSkuId());
    }
    List<OmsCartItem> cartItemList = cartItemMapper.selectByExample(example);
    if (!CollectionUtils.isEmpty(cartItemList)) {
      return cartItemList.get(0);
    }
    return null;
  }

  @Override
  public List<OmsCartItem> list(Long memberId) {
    List<OmsCartItem> cartItemList = cartItemCacheService.getCartItemListOfMember(memberId);
    if (cartItemList != null) {
      return cartItemList;
    }
    OmsCartItemExample example = new OmsCartItemExample();
    example.createCriteria().andDeleteStatusEqualTo(0).andMemberIdEqualTo(memberId);
    cartItemList = cartItemMapper.selectByExample(example);
    if (cartItemList != null) {
      cartItemCacheService.setCartItemListOfMember(memberId, cartItemList);
    }
    return cartItemList;
  }

  @Override
  public List<CartPromotionItem> listPromotion(Long memberId, List<Long> cartIds) {
    List<OmsCartItem> cartItemList = list(memberId);
    if (CollUtil.isNotEmpty(cartIds)) {
      cartItemList =
          cartItemList.stream()
              .filter(item -> cartIds.contains(item.getId()))
              .collect(Collectors.toList());
    }
    List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
    if (!CollectionUtils.isEmpty(cartItemList)) {
      cartPromotionItemList = promotionService.calcCartPromotion(cartItemList);
    }
    return cartPromotionItemList;
  }

  @Override
  public int updateQuantity(Long id, Long memberId, Integer quantity) {
    OmsCartItem cartItem = new OmsCartItem();
    cartItem.setQuantity(quantity);
    OmsCartItemExample example = new OmsCartItemExample();
    example
        .createCriteria()
        .andDeleteStatusEqualTo(0)
        .andIdEqualTo(id)
        .andMemberIdEqualTo(memberId);
    int count = cartItemMapper.updateByExampleSelective(cartItem, example);
    if (count > 0) {
      cartItemCacheService.delCartItemListOfMember(memberId);
    }
    return count;
  }

  @Override
  public int delete(Long memberId, List<Long> ids) {
    OmsCartItem cartItem = new OmsCartItem();
    cartItem.setDeleteStatus(1);
    OmsCartItemExample example = new OmsCartItemExample();
    example.createCriteria().andIdIn(ids).andMemberIdEqualTo(memberId);
    int count = cartItemMapper.updateByExampleSelective(cartItem, example);
    if (count > 0) {
      cartItemCacheService.delCartItemListOfMember(memberId);
    }
    return count;
  }

  @Override
  public CartProduct getCartProduct(Long productId) {
    return productDao.getCartProduct(productId);
  }

  @Override
  public int updateAttr(OmsCartItem cartItem) {
    // 删除原购物车信息
    OmsCartItem updateCart = new OmsCartItem();
    updateCart.setId(cartItem.getId());
    updateCart.setModifyDate(new Date());
    updateCart.setDeleteStatus(1);
    cartItemMapper.updateByPrimaryKeySelective(updateCart);
    cartItem.setId(null);
    add(cartItem);
    return 1;
  }

  @Override
  public int clear(Long memberId) {
    OmsCartItem record = new OmsCartItem();
    record.setDeleteStatus(1);
    OmsCartItemExample example = new OmsCartItemExample();
    example.createCriteria().andMemberIdEqualTo(memberId);
    return cartItemMapper.updateByExampleSelective(record, example);
  }
}
