package com.accounting.mapper;

import com.accounting.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface AdminMapper extends BaseMapper<Admin> {
    Admin selectByUsername(String username);
}