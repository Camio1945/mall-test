package com.macro.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.macro.mall.mapper.CmsSubjectMapper;
import com.macro.mall.model.CmsSubject;
import com.macro.mall.model.CmsSubjectExample;
import com.macro.mall.service.CmsSubjectService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 商品专题管理Service实现类 Created by macro on 2018/6/1. */
@Service
public class CmsSubjectServiceImpl implements CmsSubjectService {
  @Autowired private CmsSubjectMapper subjectMapper;

  @Override
  public List<CmsSubject> listAll() {
    return subjectMapper.selectByExample(new CmsSubjectExample());
  }

  @Override
  public List<CmsSubject> list(String keyword, Integer pageNum, Integer pageSize) {
    PageHelper.startPage(pageNum, pageSize);
    CmsSubjectExample example = new CmsSubjectExample();
    CmsSubjectExample.Criteria criteria = example.createCriteria();
    if (!StrUtil.isEmpty(keyword)) {
      criteria.andTitleLike("%" + keyword + "%");
    }
    return subjectMapper.selectByExample(example);
  }
}
