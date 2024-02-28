package com.macro.mall.dao;

import com.macro.mall.model.PmsProductLadder;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/** 会员阶梯价格自定义Dao Created by macro on 2018/4/26. */
public interface PmsProductLadderDao {
  /** 批量创建 */
  int insertList(@Param("list") List<PmsProductLadder> productLadderList);
}
