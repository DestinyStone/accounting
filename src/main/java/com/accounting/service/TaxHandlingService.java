package com.accounting.service;

import com.accounting.entity.TaxHandling;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 税务处理服务接口
 * 
 * @author Accounting Platform
 * @version 1.0.0
 */
public interface TaxHandlingService extends IService<TaxHandling> {
    /**
     * 保存税务处理记录（仅内部使用，只能通过业务自动生成）
     * 外部调用已禁止，只能通过业务流程自动调用
     * 
     * @param taxHandling 税务处理对象
     * @return 保存后的对象
     */
    TaxHandling saveTaxHandling(TaxHandling taxHandling);
    
    /**
     * 更新税务处理记录（仅内部使用，仅允许更新状态和缴税日期）
     * 外部调用已禁止
     * 
     * @param taxHandling 税务处理对象
     * @return 更新后的对象
     */
    TaxHandling updateTaxHandling(TaxHandling taxHandling);
    
    /**
     * 申报税务
     * 
     * @param id 税务记录ID
     * @return 是否成功
     */
    boolean declareTax(Long id);
    
    /**
     * 缴纳税务
     * 
     * @param id 税务记录ID
     * @return 是否成功
     */
    boolean payTax(Long id);
}
