package com.macro.mall.dao;

import com.macro.mall.model.PmsProductFullReduction;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/** 商品满减自定义Dao Created by macro on 2018/4/26. */
public interface PmsProductFullReductionDao {
  /** 批量创建 */
  int insertList(@Param("list") List<PmsProductFullReduction> productFullReductionList);
}
