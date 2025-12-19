package com.accounting.service;

import com.accounting.entity.TaxHandling;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TaxHandlingService extends IService<TaxHandling> {
    TaxHandling saveTaxHandling(TaxHandling taxHandling);
    TaxHandling updateTaxHandling(TaxHandling taxHandling);
    boolean declareTax(Long id);
    boolean payTax(Long id);
}
