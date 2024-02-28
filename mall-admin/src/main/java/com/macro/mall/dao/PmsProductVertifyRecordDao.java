package com.macro.mall.dao;

import com.macro.mall.model.PmsProductVertifyRecord;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/** 商品审核日志管理自定义Dao Created by macro on 2018/4/27. */
public interface PmsProductVertifyRecordDao {
  /** 批量创建 */
  int insertList(@Param("list") List<PmsProductVertifyRecord> list);
}
