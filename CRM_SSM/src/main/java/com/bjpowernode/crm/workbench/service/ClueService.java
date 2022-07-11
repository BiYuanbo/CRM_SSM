package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.bean.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    //保存创建的线索
    int insertClue(Clue clue);

    //根据条件分页查询市场列表
    List<Clue> selectClueByConditionForPage(Map<String,Object> map);

    //根据条件查询市场列表总条数
    int selectCountOfClueByCondition(Map<String,Object> map);

    //根据id查询线索信息
    Clue selectClueForDetailById(String id);

    //保存线索转换
    void saveConvert(Map<String,Object> map);
}
