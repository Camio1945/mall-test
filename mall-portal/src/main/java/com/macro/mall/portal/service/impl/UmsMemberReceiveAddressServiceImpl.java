package com.macro.mall.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.macro.mall.mapper.UmsMemberReceiveAddressMapper;
import com.macro.mall.model.UmsMember;
import com.macro.mall.model.UmsMemberReceiveAddress;
import com.macro.mall.model.UmsMemberReceiveAddressExample;
import com.macro.mall.portal.service.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/** 用户地址管理Service实现类 Created by macro on 2018/8/28. */
@Service
public class UmsMemberReceiveAddressServiceImpl implements UmsMemberReceiveAddressService {
  @Autowired private UmsMemberService memberService;
  @Autowired private UmsMemberReceiveAddressMapper addressMapper;
  @Autowired private UmsMemberReceiveAddressCacheService addressCacheService;

  @Override
  public int add(UmsMemberReceiveAddress address) {
    UmsMember currentMember = memberService.getCurrentMember();
    address.setMemberId(currentMember.getId());
    int count = addressMapper.insert(address);
    if (count > 0) {
      addressCacheService.delAddressListOfMember(currentMember.getId());
    }
    return count;
  }

  @Override
  public int delete(Long id) {
    UmsMember currentMember = memberService.getCurrentMember();
    UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
    example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
    int count = addressMapper.deleteByExample(example);
    if (count > 0) {
      addressCacheService.delAddressListOfMember(currentMember.getId());
    }
    return count;
  }

  @Override
  public int update(Long id, UmsMemberReceiveAddress address) {
    address.setId(null);
    UmsMember currentMember = memberService.getCurrentMember();
    UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
    example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
    if (address.getDefaultStatus() == null) {
      address.setDefaultStatus(0);
    }
    if (address.getDefaultStatus() == 1) {
      // 先将原来的默认地址去除
      UmsMemberReceiveAddress record = new UmsMemberReceiveAddress();
      record.setDefaultStatus(0);
      UmsMemberReceiveAddressExample updateExample = new UmsMemberReceiveAddressExample();
      updateExample
          .createCriteria()
          .andMemberIdEqualTo(currentMember.getId())
          .andDefaultStatusEqualTo(1);
      addressMapper.updateByExampleSelective(record, updateExample);
    }
    int count = addressMapper.updateByExampleSelective(address, example);
    if (count > 0) {
      addressCacheService.delAddressListOfMember(currentMember.getId());
    }
    return count;
  }

  @Override
  public List<UmsMemberReceiveAddress> list() {
    UmsMember currentMember = memberService.getCurrentMember();
    Long memberId = currentMember.getId();
    List<UmsMemberReceiveAddress> addressList =
        addressCacheService.getAddressListOfMember(memberId);
    if (addressList != null) {
      return addressList;
    }
    UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
    example.createCriteria().andMemberIdEqualTo(memberId);
    addressList = addressMapper.selectByExample(example);
    if (addressList != null) {
      addressCacheService.setAddressListOfMember(memberId, addressList);
    }
    return addressList;
  }

  @Override
  public UmsMemberReceiveAddress getItem(Long id) {
    UmsMember currentMember = memberService.getCurrentMember();
    UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
    Long memberId = currentMember.getId();
    List<UmsMemberReceiveAddress> addressList =
        addressCacheService.getAddressListOfMember(memberId);
    if (CollUtil.isNotEmpty(addressList)
        && (addressList.get(0).getMemberId().longValue() == memberId.longValue())) {
      return addressList.get(0);
    }
    example.createCriteria().andMemberIdEqualTo(memberId).andIdEqualTo(id);
    addressList = addressMapper.selectByExample(example);
    if (!CollectionUtils.isEmpty(addressList)) {
      addressCacheService.setAddressListOfMember(memberId, addressList);
      return addressList.get(0);
    }
    return null;
  }
}
