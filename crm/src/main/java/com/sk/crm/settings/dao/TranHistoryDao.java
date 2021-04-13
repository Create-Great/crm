package com.sk.crm.settings.dao;

import com.sk.crm.settings.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory th);

    List<TranHistory> getHistoryListByTranId(String tranId);
}
