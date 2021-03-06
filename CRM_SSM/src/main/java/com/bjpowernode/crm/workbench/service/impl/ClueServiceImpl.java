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
        //??????id??????????????????
        Clue clue = clueMapper.selectById(clueId);

        Customer customer = new Customer();
        //???????????????????????????????????????????????????
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
        //?????????????????????
        customerMapper.insertCustomer(customer);

        Contacts contacts = new Contacts();
        //??????????????????????????????????????????????????????
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
        //????????????????????????
        contactsMapper.insertContacts(contacts);

        //??????clueId?????????????????????????????????
        List<ClueRemark> crList = clueRemarkMapper.selectClueRemarkByClueId(clueId);

        //????????????????????????
        if (crList!=null&&crList.size()>0){
            //?????????????????????????????????????????????,??????????????????????????????
            CustomerRemark cur = null;
            List<CustomerRemark> curList = new ArrayList<>();

            //=============================
            ContactsRemark cor = null;
            List<ContactsRemark> corList = new ArrayList<>();

            for (ClueRemark clueRemark : crList) {
                //=============================??????
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

                //=============================?????????
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
            //=============================???????????????????????????
            customerRemarkMapper.insertCustomerRemarkByList(curList);
            //=============================??????????????????????????????
            contactsRemarkMapper.insertContactsRemarkByList(corList);
        }

        //??????clueId?????????????????????????????????????????????
        List<ClueActivityRelation> carList = clueActivityRelationMapper.getListByClueId(clueId);

        if (carList!=null&&carList.size()>0){
            //????????????????????????????????????????????????????????????????????????????????????????????????
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

        //??????????????????????????????
        String isCreateTran = (String) map.get("isCreateTran");
        if ("true".equals(isCreateTran)){
            //????????????
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

            //????????????????????????????????????
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

        //??????clueId??????????????????
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);

        //??????clueId??????????????????????????????????????????
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);

        //????????????
        clueMapper.deleteClueById(clueId);
    }
}
