package com.accounting.service;

import com.accounting.entity.RegularBusiness;
import com.baomidou.mybatisplus.extension.service.IService;

public interface RegularBusinessService extends IService<RegularBusiness> {
    RegularBusiness saveRegularBusiness(RegularBusiness regularBusiness);
    RegularBusiness updateRegularBusiness(RegularBusiness regularBusiness);
    boolean processBusiness(Long id);
}
