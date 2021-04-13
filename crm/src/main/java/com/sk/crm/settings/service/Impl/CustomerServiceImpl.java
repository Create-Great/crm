package com.sk.crm.settings.service.Impl;

import com.sk.crm.settings.dao.CustomerDao;
import com.sk.crm.settings.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;

    @Override
    public List<String> getCustomerName(String name) {

        List<String> sList = customerDao.getCustomerName(name);

        return sList;
    }
}
