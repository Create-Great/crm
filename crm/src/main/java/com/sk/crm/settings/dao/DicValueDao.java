package com.sk.crm.settings.dao;

import com.sk.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
