package com.macro.mall.portal.service;

import com.macro.mall.model.UmsMemberReceiveAddress;
import java.util.List;

/**
 * 用户地址管理缓存接口。<br>
 * 【性能优化备注】这个类是为了优化性能而添加的
 *
 * @author Camio1945
 */
public interface UmsMemberReceiveAddressCacheService {

  /**
   * 获取会员的收货地址列表
   *
   * @param memberId 会员ID
   * @return 收货地址列表，如果为 null ，表示缓存中不存在，但数据库中可能存在，但如果为 empty list，表示用户在数据库中也没有。
   */
  List<UmsMemberReceiveAddress> getAddressListOfMember(Long memberId);

  /**
   * 设置会员的收货地址列表
   *
   * @param memberId 会员ID
   * @param addressList 收货地址列表
   */
  void setAddressListOfMember(Long memberId, List<UmsMemberReceiveAddress> addressList);

  /**
   * 删除会员的收货地址列表
   *
   * @param memberId 会员ID
   */
  void delAddressListOfMember(Long memberId);
}
