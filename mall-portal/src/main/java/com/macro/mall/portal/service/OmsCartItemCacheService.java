package com.macro.mall.portal.service;

import com.macro.mall.model.OmsCartItem;
import java.util.List;

/**
 * 购物车管理缓存业务接口。<br>
 * 【性能优化备注】这个类是为了优化性能而添加的
 *
 * @author Camio1945
 */
public interface OmsCartItemCacheService {

  /**
   * 获取会员的购物车列表
   *
   * @param memberId 会员ID
   * @return 购物车列表，如果为 null ，表示缓存中不存在，但数据库中可能存在，但如果为 empty list，表示用户在数据库中也没有。
   */
  List<OmsCartItem> getCartItemListOfMember(Long memberId);

  /**
   * 设置会员的购物车列表
   *
   * @param memberId 会员ID
   * @param cartItemList 购物车列表
   */
  void setCartItemListOfMember(Long memberId, List<OmsCartItem> cartItemList);

  /**
   * 删除会员的购物车列表
   *
   * @param memberId 会员ID
   */
  void delCartItemListOfMember(Long memberId);
}
