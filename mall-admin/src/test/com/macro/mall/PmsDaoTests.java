package com.macro.mall;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.hutool.json.JSONUtil;
import com.macro.mall.dao.PmsMemberPriceDao;
import com.macro.mall.dao.PmsProductDao;
import com.macro.mall.dto.PmsProductResult;
import com.macro.mall.model.PmsMemberPrice;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class PmsDaoTests {
  private static final Logger LOGGER = LoggerFactory.getLogger(PmsDaoTests.class);

  @Autowired private PmsMemberPriceDao memberPriceDao;

  @Autowired private PmsProductDao productDao;

  @Test
  @Transactional
  @Rollback
  public void testInsertBatch() {
    List<PmsMemberPrice> list = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      PmsMemberPrice memberPrice = new PmsMemberPrice();
      memberPrice.setProductId(1L);
      memberPrice.setMemberLevelId((long) (i + 1));
      memberPrice.setMemberPrice(new BigDecimal("22"));
      list.add(memberPrice);
    }
    int count = memberPriceDao.insertList(list);
    assertEquals(5, count);
  }

  @Test
  public void testGetProductUpdateInfo() {
    PmsProductResult productResult = productDao.getUpdateInfo(7L);
    String json = JSONUtil.parse(productResult).toString();
    LOGGER.info(json);
  }
}
