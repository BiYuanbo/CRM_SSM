package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.bean.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityMapper {
    //添加市场列表
    int insertActivity(Activity activity);

    //根据条件分页查询市场列表
    List<Activity> selectActivityByConditionForPage(Map<String,Object> map);

    //根据条件查询市场列表总条数
    int selectCountOfActivityByCondition(Map<String,Object> map);

    //根据id批量删除市场活动
    int deleteActivityByIds(String[] ids);

    //根据id选择市场活动列表
    Activity selectActivityById(String id);

    //根据id修改市场活动
    int updateActivity(Activity activity);

    //查询所有的市场活动
    List<Activity> selectAllActivity();

    //选择查询市场活动
    List<Activity> selectChooseActivity(String[] ids);

    //批量保存创建的市场活动
    int insertActivityByList(List<Activity> activityList);

    //根据id查询市场活动的明细信息
    Activity selectActivityForDetailById(String id);

    //根据clueId查询该线索相关联的市场活动的明确信息
    List<Activity> selectActivityForDetailByClueId(String clueId);

    //根据名称模糊查询市场活动并且排除以及关联过的
    List<Activity> getActivityListByNameAndNotByClueId(Map<String,Object> map);

    //根据id查询市场活动的明细信息
    List<Activity> selectActivityForDetailByIds(String[] ids);

    //根据name模糊查询市场活动，并且查询那些跟clueId关联过的市场活动
    List<Activity> getActivityListByNameAndAndClueId(Map<String,Object> map);

    //根据名称模糊查询市场活动
    List<Activity> getActivityListByName(String name);
}
