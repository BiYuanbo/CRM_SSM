package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateTimeUtil;
import com.bjpowernode.crm.commons.utils.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;

    @Override
    public int insertClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> selectClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int selectCountOfClueByCondition(Map<String, Object> map) {
        return clueMapper.selectCountOfClueByCondition(map);
    }

    @Override
    public Clue selectClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public void saveConvert(Map<String, Object> map) {
        String clueId = (String) map.get("clueId");
        User user = (User) map.get(Contants.SESSION_USER);
        //根据id查询线索信息
        Clue clue = clueMapper.selectById(clueId);

        Customer customer = new Customer();
        //将查询出的线索信息封装到客户对象中
        customer.setId(UUIDUtil.getUUID());
        customer.setOwner(clue.getOwner());
        customer.setName(clue.getCompany());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setCreateBy(user.getName());
        customer.setCreateTime(DateTimeUtil.getSysTime());
        customer.setContactSummary(clue.getContactSummary());
        customer.setDescription(clue.getDescription());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setAddress(clue.getAddress());
        //保存创建的客户
        customerMapper.insertCustomer(customer);

        Contacts contacts = new Contacts();
        //将查询出的线索信息封装到联系人对象中
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(user.getName());
        contacts.setCreateTime(DateTimeUtil.getSysTime());
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());
        //保存创建的联系人
        contactsMapper.insertContacts(contacts);

        //根据clueId查询该线索下所有的备注
        List<ClueRemark> crList = clueRemarkMapper.selectClueRemarkByClueId(clueId);

        //如果该线索有备注
        if (crList!=null&&crList.size()>0){
            //把线索下的备注转换到客户备注表,以及联系人备注表一份
            CustomerRemark cur = null;
            List<CustomerRemark> curList = new ArrayList<>();

            //=============================
            ContactsRemark cor = null;
            List<ContactsRemark> corList = new ArrayList<>();

            for (ClueRemark clueRemark : crList) {
                //=============================客户
                cur = new CustomerRemark();

                cur.setId(UUIDUtil.getUUID());
                cur.setNoteContent(clueRemark.getNoteContent());
                cur.setCreateBy(clueRemark.getCreateBy());
                cur.setCreateTime(clueRemark.getCreateTime());
                cur.setEditBy(clueRemark.getEditBy());
                cur.setEditTime(clueRemark.getEditTime());
                cur.setEditFlag(clueRemark.getEditFlag());
                cur.setCustomerId(customer.getId());

                curList.add(cur);

                //=============================联系人
                cor = new ContactsRemark();

                cor.setId(UUIDUtil.getUUID());
                cor.setNoteContent(clueRemark.getNoteContent());
                cor.setCreateBy(clueRemark.getCreateBy());
                cor.setCreateTime(clueRemark.getCreateTime());
                cor.setEditBy(clueRemark.getEditBy());
                cor.setEditTime(clueRemark.getEditTime());
                cor.setEditFlag(clueRemark.getEditFlag());
                cor.setContactsId(contacts.getId());

                corList.add(cor);
            }
            //=============================保存创建的客户备注
            customerRemarkMapper.insertCustomerRemarkByList(curList);
            //=============================保存创建的联系人备注
            contactsRemarkMapper.insertContactsRemarkByList(corList);
        }

        //根据clueId查询该线索和市场活动的关联关系
        List<ClueActivityRelation> carList = clueActivityRelationMapper.getListByClueId(clueId);

        if (carList!=null&&carList.size()>0){
            //把该线索与市场活动的关联关系转换到联系人和市场活动的关联关系表中
            ContactsActivityRelation car = null;
            List<ContactsActivityRelation> list = new ArrayList<>();
            for (ClueActivityRelation clueActivityRelation : carList) {
                car = new ContactsActivityRelation();

                car.setId(UUIDUtil.getUUID());
                car.setContactsId(contacts.getId());
                car.setActivityId(clueActivityRelation.getActivityId());

                list.add(car);
            }

            contactsActivityRelationMapper.insertContactActivityRelationByList(list);
        }

        //判断是否需要创建交易
        String isCreateTran = (String) map.get("isCreateTran");
        if ("true".equals(isCreateTran)){
            //添加交易
            Transaction tran = new Transaction();

            tran.setId(UUIDUtil.getUUID());
            tran.setOwner(user.getId());
            String money = (String) map.get("money");
            tran.setMoney(money);
            String name = (String) map.get("name");
            tran.setName(name);
            String expectedDate = (String) map.get("expectedDate");
            tran.setExpectedDate(expectedDate);
            tran.setCustomerId(customer.getId());
            String stage = (String) map.get("stage");
            tran.setStage(stage);
            String activityId = (String) map.get("activityId");
            tran.setActivityId(activityId);
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getName());
            tran.setCreateTime(DateTimeUtil.getSysTime());

            transactionMapper.insertTran(tran);

            //将线索备注转换到交易备注
            if (crList!=null&&crList.size()>0){
                TransactionRemark tr = null;
                List<TransactionRemark> list = new ArrayList<>();
                for (ClueRemark clueRemark : crList) {
                    tr = new TransactionRemark();
                    tr.setId(UUIDUtil.getUUID());
                    tr.setNoteContent(clueRemark.getNoteContent());
                    tr.setCreateBy(clueRemark.getCreateBy());
                    tr.setCreateTime(clueRemark.getCreateTime());
                    tr.setEditBy(clueRemark.getEditBy());
                    tr.setEditTime(clueRemark.getEditTime());
                    tr.setEditFlag(clueRemark.getEditFlag());
                    tr.setTranId(tran.getId());

                    list.add(tr);
                }
                transactionRemarkMapper.insertTranRemarkByList(list);
            }
        }

        //根据clueId删除线索备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);

        //根据clueId删除该线索与市场活动关联关系
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);

        //删除线索
        clueMapper.deleteClueById(clueId);
    }
}
