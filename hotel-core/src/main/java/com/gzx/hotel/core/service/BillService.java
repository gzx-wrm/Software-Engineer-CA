package com.gzx.hotel.core.service;

import com.gzx.hotel.base.service.ICrudService;
import com.gzx.hotel.core.dto.BillDTO;
import com.gzx.hotel.core.po.Bill;

public interface BillService extends ICrudService<Bill> {
    BillDTO getBillById(Long billId);
}
