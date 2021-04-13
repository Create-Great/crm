package com.sk.crm.settings.service.Impl;

import com.sk.crm.settings.dao.CustomerDao;
import com.sk.crm.settings.dao.TranDao;
import com.sk.crm.settings.dao.TranHistoryDao;
import com.sk.crm.settings.domain.Customer;
import com.sk.crm.settings.domain.Tran;
import com.sk.crm.settings.domain.TranHistory;
import com.sk.crm.settings.service.TranService;
import com.sk.crm.utils.DateTimeUtil;
import com.sk.crm.utils.UUIDUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {

    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;
    @Resource
    private CustomerDao customerDao;

    /*
        （1）做添加之前,参数t里少了一项信息,客户的主键,customerId
        （2）提交表单客户上传名字，后台处理，最终保存的是customerId
		（3）查询是否有此客户，如果没有则添加客户记录
		（4）添加交易记录（注意先后顺序，添加完客户之后，才知道客户id）
		（5）添加交易历史

     */
    @Override
    public boolean save(Tran t, String customerName) {

        boolean flag = true;
        Customer cus = customerDao.getCustomerByName(customerName);

        //如果cus为null,需要创建客户
        if (cus == null){
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateBy(t.getCreateBy());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setContactSummary(t.getContactSummary());
            cus.setNextContactTime(t.getNextContactTime());
            cus.setOwner(t.getOwner());
            //添加客户
            int count1 = customerDao.save(cus);
            if (count1 != 1){
                flag = false;
            }
        }

        //将客户id封装到t对象中
        t.setCustomerId(cus.getId());

        //添加交易
        int count2 = tranDao.save(t);
        if (count2 != 1){
            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());
        int count3 = tranHistoryDao.save(th);
        if (count3 != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {

        List<TranHistory> thList = tranHistoryDao.getHistoryListByTranId(tranId);

        return thList;
    }

    @Override
    public boolean changeStage(Tran t) {

        boolean flag = true;
        int count1 = tranDao.changeStage(t);
        if (count1 != 1){
            flag = false;
        }

        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        //添加交易历史
        int count2 = tranHistoryDao.save(th);
        if (count2 != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {

        //返回total
        int total = tranDao.getTotal();

        //取得dataList
        List<Map<String,Object>> dataList = tranDao.getCharts();
        Map<String, Object> map = new HashMap<>();

        map.put("total",total);
        map.put("dataList",dataList);

        return map;
    }

}
