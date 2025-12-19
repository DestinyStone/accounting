package com.accounting.controller;

import com.accounting.common.Api;
import com.accounting.common.Response;
import com.accounting.service.FinancialReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Api.path + "/financial-report")
public class FinancialReportController {

    @Autowired
    private FinancialReportService financialReportService;

    @GetMapping("/balance-sheet/{date}")
    public Response getBalanceSheet(@PathVariable String date) {
        return Response.success(financialReportService.getBalanceSheet(date));
    }

    @GetMapping("/profit-statement/{startDate}/{endDate}")
    public Response getProfitStatement(@PathVariable String startDate, @PathVariable String endDate) {
        return Response.success(financialReportService.getProfitStatement(startDate, endDate));
    }

    @GetMapping("/cash-flow/{startDate}/{endDate}")
    public Response getCashFlowStatement(@PathVariable String startDate, @PathVariable String endDate) {
        return Response.success(financialReportService.getCashFlowStatement(startDate, endDate));
    }
}
