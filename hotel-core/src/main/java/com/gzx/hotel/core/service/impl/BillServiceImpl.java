package com.gzx.hotel.core.service.impl;

import com.gzx.hotel.base.service.impl.ICrudServiceImpl;
import com.gzx.hotel.core.dao.BillDao;
import com.gzx.hotel.core.dto.BillDTO;
import com.gzx.hotel.core.po.Bill;
import com.gzx.hotel.core.service.BillService;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl extends ICrudServiceImpl<Bill> implements BillService {
    @Override
    public BillDTO getBillById(Long billId) {
        return ((BillDao) baseMapper).getBillById(billId);
    }
}
