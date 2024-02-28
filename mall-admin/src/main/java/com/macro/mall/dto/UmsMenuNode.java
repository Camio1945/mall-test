package com.macro.mall.dto;

import com.macro.mall.model.UmsMenu;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** 后台菜单节点封装 Created by macro on 2020/2/4. */
@Getter
@Setter
public class UmsMenuNode extends UmsMenu {
  @ApiModelProperty(value = "子级菜单")
  private List<UmsMenuNode> children;
}
