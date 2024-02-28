package com.macro.mall.dao;

import com.macro.mall.model.CmsPrefrenceAreaProductRelation;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/** 优选和商品关系自定义Dao Created by macro on 2018/4/26. */
public interface CmsPrefrenceAreaProductRelationDao {
  /** 批量创建 */
  int insertList(
      @Param("list") List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList);
}
