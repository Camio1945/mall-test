package com.macro.mall.portal.dao;

import com.macro.mall.common.log.TrackExecutionTime;
import com.macro.mall.model.OmsOrderItem;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/** 订单商品信息管理自定义Dao Created by macro on 2018/9/3. */
public interface PortalOrderItemDao {
  /** 批量插入 */
  // @TrackExecutionTime
  int insertList(@Param("list") List<OmsOrderItem> list);
}
