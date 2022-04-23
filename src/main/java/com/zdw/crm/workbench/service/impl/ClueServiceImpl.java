package com.zdw.crm.workbench.service.impl;

import com.zdw.crm.utils.DateTimeUtil;
import com.zdw.crm.utils.SqlSessionUtil;
import com.zdw.crm.utils.UUIDUtil;
import com.zdw.crm.workbench.dao.*;
import com.zdw.crm.workbench.domain.*;
import com.zdw.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    //线索相关表
    private  ClueDao clueDao=SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    //客户相关表
    private CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao=SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    //联系人相关表
    private ContactsDao contactsDao=SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    private ContactsRemarkDao contactsRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);

    //交易相关表
    private TranDao tranDao=SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


    @Override
    public List<Clue> getClue() {
        List<Clue>  clueList= clueDao.getClue();
        return clueList;
    }

    @Override
    public Boolean savaClue(Clue clue) {
        Boolean success=false;
        int cont=clueDao.savaClue(clue);
        if(cont==1){
            success=true;
        }
        return success;
    }

    @Override
    public Clue getDetail(String id) {
       Clue clue= clueDao.getDetail(id);
        return clue;
    }

    @Override
    public Boolean convert(String createBy, Tran tran, String clueId) {
        String createTime =DateTimeUtil.getSysTime();

        Boolean flag=true;

        //(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
       Clue clue= clueDao.getCluebyClueId(clueId);
       //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String name=clue.getCompany();
        Customer customer=customerDao.getcustomerbyNmae(name);
        if(customer==null){
            //表示客户不存在，新建客户
            customer=new Customer();
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(clue.getCompany());
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setContactSummary(clue.getContactSummary());
            customer.setDescription(clue.getDescription());
        }

        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts=new Contacts();

        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setId(UUIDUtil.getUUID());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateTime(createTime);
        contacts.setCreateBy(createBy);
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());

        int cont=contactsDao.sava(contacts);
        if(cont!=1){
            flag=false;
        }

        //(4) 线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarkList= clueRemarkDao.getRemarkByclueId(clueId);

        CustomerRemark customerRemark=new CustomerRemark();//客户备注
        ContactsRemark contactsRemark=new ContactsRemark();//联系人备注
        for(ClueRemark clueRemark:clueRemarkList){
            //转化到客户备注
            customerRemark.setNoteContent(clueRemark.getNoteContent());
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setCreateBy(clueRemark.getCreateBy());
            customerRemark.setCreateTime(clueRemark.getCreateTime());
            int cont2=customerRemarkDao.sava(customerRemark);
            if(cont2!=1){
                flag=false;
            }
            //转化联系人备注
            contactsRemark.setNoteContent(clueRemark.getNoteContent());
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setEditFlag("0");
            contactsRemark.setCreateBy(clueRemark.getCreateBy());
            contactsRemark.setCreateTime(clueRemark.getCreateTime());
            contactsRemark.setContactsId(contacts.getId());
            int cont3=contactsRemarkDao.sava(contactsRemark);
            if(cont3!=1){
                flag=false;
            }
        }
        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<String> activityIds = clueActivityRelationDao.getActivityId(clueId);
        ContactsActivityRelation contactsActivityRelation=new ContactsActivityRelation();
        for(String activityId:activityIds){

            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());

            int cont4 =contactsActivityRelationDao.sava(contactsActivityRelation);
            if (cont4!=1){
                flag=false;
            }
        }
        //(6) 如果有创建交易需求，创建一条交易
        if(tran!=null){
            //表示要创建交易
            tran.setSource(clue.getSource());
            tran.setDescription(clue.getDescription());
            tran.setCustomerId(customer.getId());
            tran.setContactSummary(clue.getContactSummary());
            tran.setContactsId(contacts.getId());
            tran.setOwner(clue.getOwner());
            tran.setNextContactTime(clue.getNextContactTime());
               int cont5=tranDao.sava(tran);
            if(cont5!=1){
                flag=false;
            }
            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory=new TranHistory();

            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateTime(createTime);
            tranHistory.setCreateBy(createBy);
            tranHistory.setTranId(tran.getId());

            int cont6=tranHistoryDao.sava(tranHistory);
            if(cont6!=1){
                flag=false;
            }
        }
        //(8) 删除线索备注
        int cont7=clueRemarkDao.delRemarkbyClueId(clueId);
        /*if(cont7==1){
            flag=false;
        }*/
         //       (9) 删除线索和市场活动的关系
        int cont8=clueActivityRelationDao.delbyClueId(clueId);
        /*if(cont8!=1){
            flag=false;
        }*/
          //      (10) 删除线索
        int cont9=clueDao.delbyClueId(clueId);
        if(cont9!=1){
            flag=false;
        }


        return flag;
    }

}
