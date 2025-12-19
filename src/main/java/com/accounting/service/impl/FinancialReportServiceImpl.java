package com.accounting.service.impl;

import com.accounting.mapper.FinancialReportMapper;
import com.accounting.service.FinancialReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinancialReportServiceImpl implements FinancialReportService {

    @Autowired
    private FinancialReportMapper financialReportMapper;

    @Override
    public Map<String, Object> getBalanceSheet(String date) {
        List<Map<String, Object>> data = financialReportMapper.getBalanceSheet(date);
        
        Map<String, Object> result = new HashMap<>();
        result.put("data", data);
        
        // 计算总资产和总负债及所有者权益
        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal totalLiabilities = BigDecimal.ZERO;
        BigDecimal totalEquity = BigDecimal.ZERO;
        
        for (Map<String, Object> item : data) {
            String type = (String) item.get("type");
            BigDecimal amount = (BigDecimal) item.get("amount");
            
            if ("asset".equals(type)) {
                totalAssets = totalAssets.add(amount);
            } else if ("liability".equals(type)) {
                totalLiabilities = totalLiabilities.add(amount);
            } else if ("equity".equals(type)) {
                totalEquity = totalEquity.add(amount);
            }
        }
        
        result.put("totalAssets", totalAssets);
        result.put("totalLiabilities", totalLiabilities);
        result.put("totalEquity", totalEquity);
        
        return result;
    }

    @Override
    public Map<String, Object> getProfitStatement(String startDate, String endDate) {
        List<Map<String, Object>> data = financialReportMapper.getProfitStatement(startDate, endDate);
        
        Map<String, Object> result = new HashMap<>();
        result.put("data", data);
        
        // 计算总收入、总成本、净利润
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal netProfit = BigDecimal.ZERO;
        
        for (Map<String, Object> item : data) {
            String type = (String) item.get("type");
            BigDecimal amount = (BigDecimal) item.get("amount");
            
            if ("revenue".equals(type)) {
                totalRevenue = totalRevenue.add(amount);
            } else if ("cost".equals(type)) {
                totalCost = totalCost.add(amount);
            } else if ("profit".equals(type)) {
                netProfit = amount;
            }
        }
        
        result.put("totalRevenue", totalRevenue);
        result.put("totalCost", totalCost);
        result.put("netProfit", netProfit);
        
        return result;
    }

    @Override
    public Map<String, Object> getCashFlowStatement(String startDate, String endDate) {
        List<Map<String, Object>> data = financialReportMapper.getCashFlowStatement(startDate, endDate);
        
        Map<String, Object> result = new HashMap<>();
        result.put("data", data);
        
        // 计算经营活动、投资活动、筹资活动现金流
        BigDecimal operatingCashFlow = BigDecimal.ZERO;
        BigDecimal investingCashFlow = BigDecimal.ZERO;
        BigDecimal financingCashFlow = BigDecimal.ZERO;
        BigDecimal netCashFlow = BigDecimal.ZERO;
        
        for (Map<String, Object> item : data) {
            String type = (String) item.get("type");
            BigDecimal amount = (BigDecimal) item.get("amount");
            
            if ("operating".equals(type)) {
                operatingCashFlow = operatingCashFlow.add(amount);
            } else if ("investing".equals(type)) {
                investingCashFlow = investingCashFlow.add(amount);
            } else if ("financing".equals(type)) {
                financingCashFlow = financingCashFlow.add(amount);
            } else if ("net".equals(type)) {
                netCashFlow = amount;
            }
        }
        
        result.put("operatingCashFlow", operatingCashFlow);
        result.put("investingCashFlow", investingCashFlow);
        result.put("financingCashFlow", financingCashFlow);
        result.put("netCashFlow", netCashFlow);
        
        return result;
    }
}