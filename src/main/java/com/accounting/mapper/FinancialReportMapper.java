package com.accounting.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FinancialReportMapper {
    // 资产负债表
    List<Map<String, Object>> getBalanceSheet(@Param("date") String date);
    
    // 利润表
    List<Map<String, Object>> getProfitStatement(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    // 现金流量表
    List<Map<String, Object>> getCashFlowStatement(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
