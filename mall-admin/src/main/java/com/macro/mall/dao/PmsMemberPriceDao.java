package com.macro.mall.dao;

import com.macro.mall.model.PmsMemberPrice;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/** 会员价格管理自定义Dao Created by macro on 2018/4/26. */
public interface PmsMemberPriceDao {
  /** 批量创建 */
  int insertList(@Param("list") List<PmsMemberPrice> memberPriceList);
}
