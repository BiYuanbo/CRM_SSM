package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.bean.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int insertActivity(Activity activity);

    List<Activity> selectActivityByConditionForPage(Map<String,Object> map);

    int selectCountOfActivityByCondition(Map<String,Object> map);

    int deleteActivityByIds(String[] ids);

    Activity selectActivityById(String id);

    int updateActivity(Activity activity);

    List<Activity> selectAllActivity();

    List<Activity> selectChooseActivity(String[] ids);

    int insertActivityByList(List<Activity> activityList);

    Activity selectActivityForDetailById(String id);

    //根据clueId查询该线索相关联的市场活动的明确xinxi
    List<Activity> selectActivityForDetailByClueId(String clueId);

    //根据名称模糊查询市场活动并且排除以及关联过的
    List<Activity> getActivityListByNameAndNotByClueId(Map<String,Object> map);

    //根据id查询市场活动的明细信息
    List<Activity> selectActivityForDetailByIds(String[] ids);

    //根据name模糊查询市场活动，并且查询那些跟clueId关联过的市场活动
    List<Activity> getActivityListByNameAndAndClueId(Map<String,Object> map);

    ////根据名称模糊查询市场活动
    List<Activity> getActivityListByName(String name);
}
