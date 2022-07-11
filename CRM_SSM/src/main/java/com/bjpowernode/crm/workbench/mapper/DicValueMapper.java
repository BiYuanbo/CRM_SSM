package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.bean.DicValue;

import java.util.List;

public interface DicValueMapper {

    //根据typeCode查询数据字典值
    List<DicValue> selectDicValueByTypeCode(String typeCode);
}
