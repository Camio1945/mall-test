package com.macro.mall.portal.controller;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mapper.*;
import com.macro.mall.model.*;
import com.macro.mall.portal.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.LongStream;
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
  @Autowired private OmsCartItemService cartItemService;
  @Autowired private UmsMemberReceiveAddressMapper addressMapper;
  @Autowired private OmsCartItemMapper cartItemMapper;
  @Autowired private UmsMemberReceiveAddressCacheService addressCacheService;
  @Autowired private OmsOrderMapper orderMapper;
  @Autowired private OmsPortalOrderCacheService orderCacheService;

  @ApiOperation("准备（主要是缓存数据）")
  @GetMapping(value = "/prepare")
  @ResponseBody
  public CommonResult<String> prepare() {
    LongStream.range(0, 10001)
        .parallel()
        .forEach(i -> loadCache("member" + i, i + 12));
    generatePreOrder();
    return CommonResult.success("操作成功");
  }

  private void loadCache(String userName, Long memberId) {
    memberCacheService.getMember(userName);
    cartItemCacheService.delCartItemListOfMember(memberId);
    cartItemService.list(memberId);
    addressCacheService.delAddressListOfMember(memberId);
    UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
    example.createCriteria().andMemberIdEqualTo(memberId);
    List<UmsMemberReceiveAddress> addressList = addressMapper.selectByExample(example);
    if (addressList != null) {
      addressCacheService.setAddressListOfMember(memberId, addressList);
    }
  }

  /** 生成预订单 */
  private void generatePreOrder() {
    OmsOrderExample example = new OmsOrderExample();
    example.setOrderByClause("id asc");
    example.createCriteria().andDeleteStatusEqualTo(0).andIsPreEqualTo((byte) 1);
    int total = 10100 - (int) orderMapper.countByExample(example);
    for (int i = 0; i < total; i++) {
      OmsOrder order = new OmsOrder();
      order.setMemberId(0L);
      order.setReceiverName("");
      order.setReceiverPhone("");
      order.setDeleteStatus(0);
      order.setIsPre((byte) 1);
      orderMapper.insert(order);
    }
    List<OmsOrder> omsOrders = orderMapper.selectByExample(example);
    orderCacheService.putPreOrderList(omsOrders);
  }

  @ApiOperation("临时测试")
  @GetMapping(value = "/temp")
  @ResponseBody
  public CommonResult<String> temp() {
    return CommonResult.success("" + orderCacheService.popPreOrder());
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
    loadOnePageOfPreOrders();
    return CommonResult.success("操作成功");
  }

  private void loadOnePageOfPreOrders() {
    OmsOrderExample example = new OmsOrderExample();
    example.setOrderByClause("id asc");
    example.createCriteria().andDeleteStatusEqualTo(0).andIsPreEqualTo((byte) 1);
    int total = 10100 - (int) orderMapper.countByExample(example);
    for (int i = 0; i < total; i++) {
      OmsOrder order = new OmsOrder();
      order.setMemberId(0L);
      order.setReceiverName("");
      order.setReceiverPhone("");
      order.setDeleteStatus(0);
      order.setIsPre((byte) 1);
      orderMapper.insert(order);
    }
    PageHelper.startPage(1, 10);
    List<OmsOrder> omsOrders = orderMapper.selectByExample(example);
    orderCacheService.putPreOrderList(omsOrders);
  }

  @ApiOperation("版本号")
  @GetMapping(value = "/version")
  @ResponseBody
  public CommonResult<String> version() {
    return CommonResult.success("4.0");
  }
}
