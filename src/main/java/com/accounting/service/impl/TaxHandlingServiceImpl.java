package com.accounting.service.impl;

import com.accounting.entity.TaxHandling;
import com.accounting.mapper.TaxHandlingMapper;
import com.accounting.service.TaxHandlingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxHandlingServiceImpl extends ServiceImpl<TaxHandlingMapper, TaxHandling> implements TaxHandlingService {

    @Autowired
    private TaxHandlingMapper taxHandlingMapper;

    @Override
    public TaxHandling saveTaxHandling(TaxHandling taxHandling) {
        taxHandlingMapper.insert(taxHandling);
        return taxHandling;
    }

    @Override
    public TaxHandling updateTaxHandling(TaxHandling taxHandling) {
        taxHandlingMapper.updateById(taxHandling);
        return taxHandling;
    }

    @Override
    public boolean declareTax(Long id) {
        TaxHandling taxHandling = new TaxHandling();
        taxHandling.setId(id);
        taxHandling.setStatus(1); // 已申报
        return taxHandlingMapper.updateById(taxHandling) > 0;
    }

    @Override
    public boolean payTax(Long id) {
        TaxHandling taxHandling = new TaxHandling();
        taxHandling.setId(id);
        taxHandling.setStatus(2); // 已缴款
        taxHandling.setPaymentDate(new java.util.Date());
        return taxHandlingMapper.updateById(taxHandling) > 0;
    }
}