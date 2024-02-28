package com.macro.mall.portal.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.UmsMember;
import com.macro.mall.portal.domain.HomeContentResult;
import com.macro.mall.portal.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 调试控制器（仅用于开发调试）
 *
 * @author Camio1945
 */
@Controller
@Api(tags = "HomeController")
@Tag(name = "HomeController", description = "首页内容管理")
@RequestMapping("/debug")
public class DebugController {
  @Autowired private UmsMemberService memberService;
  @Autowired private UmsMemberCacheService memberCacheService;
  @Autowired private OmsCartItemCacheService cartItemCacheService;
  @Autowired private UmsMemberReceiveAddressCacheService addressCacheService;

  @ApiOperation("准备（主要是缓存数据库）")
  @GetMapping(value = "/prepare")
  @ResponseBody
  public CommonResult<String> prepare() {
    long startMemberId = 12;
    long endMemberId = startMemberId + 9999;
    for (long memberId = startMemberId; memberId <= endMemberId; memberId++) {
      String userName = "member" + (memberId - startMemberId);
      memberCacheService.getMember(userName);
      cartItemCacheService.delCartItemListOfMember(memberId);
      cartItemCacheService.getCartItemListOfMember(memberId);
      addressCacheService.delAddressListOfMember(memberId);
      addressCacheService.getAddressListOfMember(memberId);
    }
    return CommonResult.success("操作成功");
  }
}
