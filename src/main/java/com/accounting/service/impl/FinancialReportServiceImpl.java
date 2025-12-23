package com.accounting.service.impl;

import com.accounting.mapper.FinancialReportMapper;
import com.accounting.service.FinancialReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        
        // 分类整理数据
        List<Map<String, Object>> assets = new ArrayList<>();
        List<Map<String, Object>> liabilities = new ArrayList<>();
        List<Map<String, Object>> equity = new ArrayList<>();
        
        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal totalLiabilities = BigDecimal.ZERO;
        BigDecimal totalEquity = BigDecimal.ZERO;
        
        for (Map<String, Object> item : data) {
            Integer type = (Integer) item.get("type");
            BigDecimal amount = item.get("amount") != null ? 
                new BigDecimal(item.get("amount").toString()) : BigDecimal.ZERO;
            
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("name", item.get("name"));
            itemMap.put("amount", amount);
            
            if (type != null) {
                if (type == 1) {  // 资产
                    assets.add(itemMap);
                    totalAssets = totalAssets.add(amount.abs());
                } else if (type == 2) {  // 负债
                    liabilities.add(itemMap);
                    totalLiabilities = totalLiabilities.add(amount.abs());
                } else if (type == 4) {  // 所有者权益
                    equity.add(itemMap);
                    totalEquity = totalEquity.add(amount.abs());
                }
            }
        }
        
        // 添加合计行
        Map<String, Object> assetsTotal = new HashMap<>();
        assetsTotal.put("name", "资产总计");
        assetsTotal.put("amount", totalAssets);
        assets.add(assetsTotal);
        
        Map<String, Object> liabilitiesTotal = new HashMap<>();
        liabilitiesTotal.put("name", "负债合计");
        liabilitiesTotal.put("amount", totalLiabilities);
        liabilities.add(liabilitiesTotal);
        
        Map<String, Object> equityTotal = new HashMap<>();
        equityTotal.put("name", "所有者权益合计");
        equityTotal.put("amount", totalEquity);
        equity.add(equityTotal);
        
        Map<String, Object> total = new HashMap<>();
        total.put("name", "负债和所有者权益总计");
        total.put("amount", totalLiabilities.add(totalEquity));
        liabilities.add(total);
        
        result.put("assets", assets);
        result.put("liabilities", liabilities);
        result.put("equity", equity);
        result.put("totalAssets", totalAssets);
        result.put("totalLiabilities", totalLiabilities);
        result.put("totalEquity", totalEquity);
        
        return result;
    }

    @Override
    public Map<String, Object> getProfitStatement(String startDate, String endDate) {
        List<Map<String, Object>> data = financialReportMapper.getProfitStatement(startDate, endDate);
        
        Map<String, Object> result = new HashMap<>();
        
        // 分类整理数据
        List<Map<String, Object>> revenue = new ArrayList<>();
        List<Map<String, Object>> cost = new ArrayList<>();
        
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        
        for (Map<String, Object> item : data) {
            String type = (String) item.get("type");
            Object amountObj = item.get("amount");
            BigDecimal amount = BigDecimal.ZERO;
            
            if (amountObj != null) {
                if (amountObj instanceof BigDecimal) {
                    amount = (BigDecimal) amountObj;
                } else if (amountObj instanceof Number) {
                    amount = BigDecimal.valueOf(((Number) amountObj).doubleValue());
                } else {
                    amount = new BigDecimal(amountObj.toString());
                }
            }
            
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("name", item.get("name"));
            itemMap.put("amount", amount);
            
            if ("revenue".equals(type)) {
                revenue.add(itemMap);
                totalRevenue = totalRevenue.add(amount.abs());
            } else if ("cost".equals(type)) {
                cost.add(itemMap);
                totalCost = totalCost.add(amount.abs());
            }
        }
        
        // 计算利润
        BigDecimal operatingProfit = totalRevenue.subtract(totalCost);
        BigDecimal netProfit = operatingProfit;  // 简化处理，不考虑所得税等
        
        // 添加合计行
        Map<String, Object> revenueTotal = new HashMap<>();
        revenueTotal.put("name", "营业收入合计");
        revenueTotal.put("amount", totalRevenue);
        revenue.add(revenueTotal);
        
        Map<String, Object> costTotal = new HashMap<>();
        costTotal.put("name", "营业成本合计");
        costTotal.put("amount", totalCost);
        cost.add(costTotal);
        
        Map<String, Object> profit = new HashMap<>();
        profit.put("name", "营业利润");
        profit.put("amount", operatingProfit);
        
        Map<String, Object> netProfitMap = new HashMap<>();
        netProfitMap.put("name", "净利润");
        netProfitMap.put("amount", netProfit);
        
        result.put("revenue", revenue);
        result.put("cost", cost);
        result.put("profit", profit);
        result.put("netProfit", netProfitMap);
        result.put("totalRevenue", totalRevenue);
        result.put("totalCost", totalCost);
        result.put("netProfit", netProfit);
        
        return result;
    }

    @Override
    public Map<String, Object> getCashFlowStatement(String startDate, String endDate) {
        List<Map<String, Object>> data = financialReportMapper.getCashFlowStatement(startDate, endDate);
        
        Map<String, Object> result = new HashMap<>();
        
        // 分类整理数据
        List<Map<String, Object>> operating = new ArrayList<>();
        List<Map<String, Object>> investing = new ArrayList<>();
        List<Map<String, Object>> financing = new ArrayList<>();
        
        BigDecimal operatingTotal = BigDecimal.ZERO;
        BigDecimal investingTotal = BigDecimal.ZERO;
        BigDecimal financingTotal = BigDecimal.ZERO;
        
        for (Map<String, Object> item : data) {
            String type = (String) item.get("type");
            Object amountObj = item.get("amount");
            BigDecimal amount = BigDecimal.ZERO;
            
            if (amountObj != null) {
                if (amountObj instanceof BigDecimal) {
                    amount = (BigDecimal) amountObj;
                } else if (amountObj instanceof Number) {
                    amount = BigDecimal.valueOf(((Number) amountObj).doubleValue());
                } else {
                    amount = new BigDecimal(amountObj.toString());
                }
            }
            
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("name", item.get("name"));
            itemMap.put("amount", amount);
            
            if ("operating".equals(type)) {
                operating.add(itemMap);
                operatingTotal = operatingTotal.add(amount);
            } else if ("investing".equals(type)) {
                investing.add(itemMap);
                investingTotal = investingTotal.add(amount);
            } else if ("financing".equals(type)) {
                financing.add(itemMap);
                financingTotal = financingTotal.add(amount);
            }
        }
        
        // 计算净增加额
        BigDecimal netIncrease = operatingTotal.add(investingTotal).add(financingTotal);
        
        // 添加合计行
        Map<String, Object> operatingTotalMap = new HashMap<>();
        operatingTotalMap.put("name", "经营活动产生的现金流量净额");
        operatingTotalMap.put("amount", operatingTotal);
        operating.add(operatingTotalMap);
        
        Map<String, Object> investingTotalMap = new HashMap<>();
        investingTotalMap.put("name", "投资活动产生的现金流量净额");
        investingTotalMap.put("amount", investingTotal);
        investing.add(investingTotalMap);
        
        Map<String, Object> financingTotalMap = new HashMap<>();
        financingTotalMap.put("name", "筹资活动产生的现金流量净额");
        financingTotalMap.put("amount", financingTotal);
        financing.add(financingTotalMap);
        
        Map<String, Object> netIncreaseMap = new HashMap<>();
        netIncreaseMap.put("name", "现金及现金等价物净增加额");
        netIncreaseMap.put("amount", netIncrease);
        
        result.put("operatingActivities", operating);
        result.put("investingActivities", investing);
        result.put("financingActivities", financing);
        result.put("netIncreaseInCash", netIncreaseMap);
        result.put("operatingCashFlow", operatingTotal);
        result.put("investingCashFlow", investingTotal);
        result.put("financingCashFlow", financingTotal);
        result.put("netCashFlow", netIncrease);
        
        return result;
    }
}