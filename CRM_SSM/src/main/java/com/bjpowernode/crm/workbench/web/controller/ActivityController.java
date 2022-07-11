package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.bean.ReturnObject;
import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateTimeUtil;
import com.bjpowernode.crm.commons.utils.HSSFUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.bean.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    //跳转到市场活动主页面
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.selectAllUsers();

        request.setAttribute("userList",userList);

        return "workbench/activity/index";
    }

    //添加市场活动
    @RequestMapping("/workbench/activity/saveActivity.do")
    @ResponseBody
    public Object insertActivity(Activity activity, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setId(UUIDUtil.getUUID());
        activity.setCreateTime(DateTimeUtil.getSysTime());
        activity.setCreateBy(user.getName());

        System.out.println("111111111111"+activity);

        ReturnObject returnObject = new ReturnObject();
        try {
            int num = activityService.insertActivity(activity);
            if (num==1){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙碌，请稍后...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙碌，请稍后...");
        }

        return returnObject;
    }

    //根据条件查询市场活动列表
    @RequestMapping("/workbench/activity/selectActivityByConditionForPage.do")
    @ResponseBody
    public Object selectActivityByConditionForPage(String name,String owner,String startDate,String endDate,
                                                   int pageNo,int pageSize){
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        List<Activity> activityList = activityService.selectActivityByConditionForPage(map);
        int total = activityService.selectCountOfActivityByCondition(map);

        Map<String,Object> retMap = new HashMap<>();
        retMap.put("activityList",activityList);
        retMap.put("total",total);

        return retMap;
    }

    //根据id批量删除市场活动
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(HttpServletRequest request){
        String[] ids = request.getParameterValues("id");

        ReturnObject ro = new ReturnObject();
        try {
            int result = activityService.deleteActivityByIds(ids);
            if (result>0){
                ro.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                ro.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统忙碌，请稍后...");
            }
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统忙碌，请稍后...");
        }

        return ro;
    }

    //根据id查询市场活动
    @RequestMapping("/workbench/activity/selectActivityById.do")
    @ResponseBody
    public Object selectActivityById(String id){
        return activityService.selectActivityById(id);
    }

    //根据id更新市场活动
    @RequestMapping("/workbench/activity/updateActivityById.do")
    @ResponseBody
    public Object updateActivityById(Activity activity,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        activity.setEditBy(user.getName());
        activity.setEditTime(DateTimeUtil.getSysTime());

        ReturnObject ro = new ReturnObject();
        try {
            int result = activityService.updateActivity(activity);

            if(result==1){
                ro.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                ro.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统忙碌，请稍后");
            }
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统忙碌，请稍后");
        }

        return  ro;
    }

    //测试文件下载功能
    /*@RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws IOException {
        //设置响应类型
        response.setContentType("application/octet-stream;charset:utf-8");

        //获取输出流
        OutputStream out = response.getOutputStream();

        //可以设置响应头信息，使浏览器接收到响应信息之后，直接激活文件下载窗口，即使能打开也不打开
        response.addHeader("Content-Disposition","attachment;filename=myStudentList.xls");

        //读取excel文件，输出到浏览器
        InputStream is = new FileInputStream("D:\\IdeaProjects\\CRM\\CRM_SSM\\studentList.xls");

        byte[] bytes = new byte[256];

        int len = 0;

        while ((len = is.read(bytes)) != -1){
            out.write(bytes,0,len);
        }

        is.close();

        out.flush();
    }*/

    //导出市场活动
    @RequestMapping("/workbench/activity/exportAllActivity.do")
    public void exportAllActivity(HttpServletResponse response) throws IOException {
        List<Activity> activityList = activityService.selectAllActivity();

        //创建excel文件
        HSSFWorkbook hw = new HSSFWorkbook();
        //创建页
        HSSFSheet sheet = hw.createSheet("市场活动列表");
        //创建行
        HSSFRow row = sheet.createRow(0);
        //创建列
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");

        if (activityList != null && activityList.size() > 0) {
            Activity activity = null;
            for (int i = 0; i < activityList.size(); i++) {
                activity = activityList.get(i);

                row = sheet.createRow(i + 1);

                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

        //============================优化前==============================
        //根据hw对象生成excel文件
        /*OutputStream os = new FileOutputStream("D:\\IdeaProjects\\CRM\\CRM_SSM\\activityList.xls");
        hw.write(os);
        //关闭资源
        os.close();
        hw.close();

        //System.out.println("==============OK==============");

        //把生成的excel文件下载到客户端
        //设置响应类型
        response.setContentType("application/octet-stream;charset:utf-8");

        //获取输出流
        OutputStream out = response.getOutputStream();

        //可以设置响应头信息，使浏览器接收到响应信息之后，直接激活文件下载窗口，即使能打开也不打开
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");

        //读取excel文件，输出到浏览器
        InputStream is = new FileInputStream("D:\\IdeaProjects\\CRM\\CRM_SSM\\activityList.xls");

        byte[] bytes = new byte[256];

        int len = 0;
        while ((len = is.read(bytes)) != -1){
            out.write(bytes,0,len);
        }

        is.close();
        out.flush();*/

        //=================优化后=================
        //设置响应类型
        response.setContentType("application/octet-stream;charset:utf-8");

        //获取输出流
        OutputStream out = response.getOutputStream();

        //可以设置响应头信息，使浏览器接收到响应信息之后，直接激活文件下载窗口，即使能打开也不打开
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");

        out.flush();

        hw.write(out);
        hw.close();
    }

    //选择导出市场活动
    @RequestMapping("/workbench/activity/exportChooseActivity.do")
    public void exportSelectActivity(HttpServletResponse response,HttpServletRequest request) throws IOException {
        String[] ids = request.getParameterValues("id");

        List<Activity> activityList = activityService.selectChooseActivity(ids);

        //创建excel文件
        HSSFWorkbook hw = new HSSFWorkbook();
        //创建页
        HSSFSheet sheet = hw.createSheet("市场活动列表");
        //创建行
        HSSFRow row = sheet.createRow(0);
        //创建列
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");

        if (activityList != null && activityList.size() > 0) {
            Activity activity = null;
            for (int i = 0; i < activityList.size(); i++) {
                activity = activityList.get(i);

                row = sheet.createRow(i + 1);

                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }
        //设置响应类型
        response.setContentType("application/octet-stream;charset:utf-8");

        //获取输出流
        OutputStream out = response.getOutputStream();

        //可以设置响应头信息，使浏览器接收到响应信息之后，直接激活文件下载窗口，即使能打开也不打开
        response.addHeader("Content-Disposition","attachment;filename=xzActivityList.xls");

        out.flush();

        hw.write(out);
        hw.close();
    }

    //测试文件上传功能
    /*@RequestMapping("/workbench/activity/fileUpload.do")
    @ResponseBody
    public Object uploadFile(String username,MultipartFile myFile) throws IOException {
        System.out.println("==============="+username+"==================");

        String fileName = myFile.getOriginalFilename();
        File file = new File("D:\\IdeaProjects\\CRM\\CRM_SSM\\"+fileName);

        myFile.transferTo(file);

        ReturnObject ro = new ReturnObject();
        ro.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        ro.setMessage("上传成功");
        return ro;
    }*/

    //上传列表数据
    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();
        try {
            //把excel文件写到磁盘目录中
            /*String fileName = activityFile.getOriginalFilename();
            File file = new File("D:\\IdeaProjects\\CRM\\CRM_SSM\\"+fileName);
            activityFile.transferTo(file);*/

            //解析excel文件，获取文件中的数据，封装成activityList
            //InputStream is = new FileInputStream("D:\\IdeaProjects\\CRM\\CRM_SSM\\"+fileName);

            InputStream is = activityFile.getInputStream();

            HSSFWorkbook hs = new HSSFWorkbook(is);

            HSSFSheet sheet = hs.getSheetAt(0);//页的下标。从零开始，依次增加

            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            List<Activity> activityList = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++){//sheet.getLastRowNum()：获取该页最后一行的下标
                row = sheet.getRow(i);//行的下标。从零开始，依次增加

                activity = new Activity();
                activity.setId(UUIDUtil.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateTimeUtil.getSysTime());
                activity.setCreateBy(user.getName());

                for (int j = 0 ; j < row.getLastCellNum(); j++){
                    cell = row.getCell(j);//列的下标。从零开始，依次增加

                    // 获取列中的数据
                    String cellValue = HSSFUtils.getCellValue(cell);
                    if(j == 0) {
                        activity.setName(cellValue);
                    } else if(j == 1){
                        activity.setStartDate(cellValue);
                    } else if(j == 2){
                        activity.setEndDate(cellValue);
                    } else if(j == 3){
                        activity.setCost(cellValue);
                    } else if(j == 4){
                        activity.setDescription(cellValue);
                    }
                }
                //每一行中所有列都封装完成之后，把activity保存到list中
                activityList.add(activity);
            }

            // 调用service层方法，保存市场活动
            int res = activityService.insertActivityByList(activityList);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setMessage(res+"条信息成功插入");
        } catch (IOException e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }

        return returnObject;
    }

    //跳转到详情信息页面
    @RequestMapping("/workbench/activity/detail.do")
    public String detail(String id,HttpServletRequest request){
        Activity activity = activityService.selectActivityForDetailById(id);

        List<ActivityRemark> remarkList = activityRemarkService.selectActivityRemarkByActivityId(id);

        request.setAttribute("activity",activity);
        request.setAttribute("remarkList",remarkList);

        return "workbench/activity/detail";
    }
}
