package com.sk.crm.settings.service;

import com.sk.crm.settings.domain.Clue;
import com.sk.crm.settings.domain.Tran;

public interface ClueService {
    boolean save(Clue clue);

    Clue detail(String id);

    boolean unBind(String id);

    boolean bund(String cid, String[] aids);

    boolean convertT(String clueId, Tran t, String createBy);
}
