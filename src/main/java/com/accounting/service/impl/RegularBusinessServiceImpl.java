package com.accounting.service.impl;

import com.accounting.entity.RegularBusiness;
import com.accounting.mapper.RegularBusinessMapper;
import com.accounting.service.RegularBusinessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegularBusinessServiceImpl extends ServiceImpl<RegularBusinessMapper, RegularBusiness> implements RegularBusinessService {

    @Autowired
    private RegularBusinessMapper regularBusinessMapper;

    @Override
    public RegularBusiness saveRegularBusiness(RegularBusiness regularBusiness) {
        regularBusinessMapper.insert(regularBusiness);
        return regularBusiness;
    }

    @Override
    public RegularBusiness updateRegularBusiness(RegularBusiness regularBusiness) {
        regularBusinessMapper.updateById(regularBusiness);
        return regularBusiness;
    }

    @Override
    public boolean processBusiness(Long id) {
        RegularBusiness regularBusiness = new RegularBusiness();
        regularBusiness.setId(id);
        regularBusiness.setStatus(1); // 已处理
        return regularBusinessMapper.updateById(regularBusiness) > 0;
    }
}