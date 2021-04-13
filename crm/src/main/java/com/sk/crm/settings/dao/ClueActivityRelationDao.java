package com.sk.crm.settings.dao;

import com.sk.crm.settings.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    int unBind(String id);

    int bund(ClueActivityRelation car);

    List<ClueActivityRelation> getListByClueId(String clueId);

    int delete(ClueActivityRelation clueActivityRelation);
}
