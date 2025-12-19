package com.accounting.service;

import java.util.List;
import java.util.Map;

public interface FinancialReportService {
    // 资产负债表
    Map<String, Object> getBalanceSheet(String date);
    
    // 利润表
    Map<String, Object> getProfitStatement(String startDate, String endDate);
    
    // 现金流量表
    Map<String, Object> getCashFlowStatement(String startDate, String endDate);
}
