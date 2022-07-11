package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.bean.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationMapper {
    //批量保存线索和市场活动列表的关系
    int insertClueActivityRelation(List<ClueActivityRelation> list);

    //解除关联
    int unbund(ClueActivityRelation clueActivityRelation);

    //根据clueId查询该线索和市场活动的关联关系
    List<ClueActivityRelation> getListByClueId(String clueId);

    //根据clueId删除该线索与市场活动关联关系
    int deleteClueActivityRelationByClueId(String clueId);
}
