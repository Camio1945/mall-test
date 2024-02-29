package com.macro.mall.portal.controller;

import cn.hutool.core.date.DateUtil;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mapper.OmsCartItemMapper;
import com.macro.mall.model.OmsCartItem;
import com.macro.mall.portal.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 调试控制器（仅用于开发调试）
 *
 * @author Camio1945
 */
@Controller
@Api(tags = "DebugController")
@Tag(name = "DebugController", description = "开发调试")
@RequestMapping("/debug")
public class DebugController {
  @Autowired private UmsMemberCacheService memberCacheService;
  @Autowired private OmsCartItemCacheService cartItemCacheService;
  @Autowired private OmsCartItemMapper cartItemMapper;
  @Autowired private UmsMemberReceiveAddressCacheService addressCacheService;

  @ApiOperation("准备（主要是缓存数据）")
  @GetMapping(value = "/prepare")
  @ResponseBody
  public CommonResult<String> prepare() {
    long startMemberId = 12;
    long endMemberId = startMemberId + 9999;
    for (long memberId = startMemberId; memberId <= endMemberId; memberId++) {
      String userName = "member" + (memberId - startMemberId);
      loadCache(userName, memberId);
    }
    return CommonResult.success("操作成功");
  }

  private void loadCache(String userName, Long memberId) {
    memberCacheService.getMember(userName);
    cartItemCacheService.delCartItemListOfMember(memberId);
    cartItemCacheService.getCartItemListOfMember(memberId);
    addressCacheService.delAddressListOfMember(memberId);
    addressCacheService.getAddressListOfMember(memberId);
  }

  @ApiOperation("恢复压测时的第一个用户的数据")
  @GetMapping(value = "/restoreMember0")
  @ResponseBody
  public CommonResult<String> restoreMember0() {
    String userName = "member0";
    Long memberId = 12L;
    Long cartItemId = 117L;
    OmsCartItem cartItem = cartItemMapper.selectByPrimaryKey(cartItemId);
    if (cartItem == null) {
      cartItem = new OmsCartItem();
      cartItem.setId(cartItemId);
      cartItem.setProductId(44L);
      cartItem.setProductSkuId(235L);
      cartItem.setMemberId(memberId);
      cartItem.setQuantity(1);
      cartItem.setPrice(new BigDecimal(369));
      cartItem.setProductPic(
          "http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/sanxing_ssd_02.jpg");
      cartItem.setProductName("三星（SAMSUNG）500GB SSD固态硬盘 M.2接口(NVMe协议)");
      cartItem.setProductSubTitle("【满血无缓存！进店抽百元E卡，部分型号白条三期免息】兼具速度与可靠性！读速高达3500MB/s，全功率模式！点击 ");
      cartItem.setProductSkuCode("202211080044001");
      cartItem.setMemberNickname("member");
      cartItem.setCreateDate(DateUtil.parse("2024/2/3  10:50:52", "yyyy/MM/dd  HH:mm:ss"));
      cartItem.setDeleteStatus(0);
      cartItem.setProductCategoryId(55L);
      cartItem.setProductBrand("三星");
      cartItem.setProductSn("100018768480");
      cartItem.setProductAttr(
          "[{\"key\":\"颜色\",\"value\":\"新品980｜NVMe PCIe3.0*4\"},{\"key\":\"版本\",\"value\":\"512GB\"}]");
      cartItemMapper.insert(cartItem);
    } else if (cartItem.getDeleteStatus() == 1) {
      cartItem.setDeleteStatus(0);
      cartItemMapper.updateByPrimaryKey(cartItem);
    }
    loadCache(userName, memberId);
    return CommonResult.success("操作成功");
  }

  @ApiOperation("版本号")
  @GetMapping(value = "/version")
  @ResponseBody
  public CommonResult<String> version() {
    return CommonResult.success("3.0");
  }
}
