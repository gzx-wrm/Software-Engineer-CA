package com.gzx.hotel.core.controller;

import com.gzx.hotel.base.controller.BaseController;
import com.gzx.hotel.base.pojo.ResponseBean;
import com.gzx.hotel.core.po.Record;
import com.gzx.hotel.core.service.RecordService;
import com.gzx.hotel.core.service.RoomService;
import com.gzx.hotel.core.utils.SecurityPrincipalUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController extends BaseController<RecordService, Record> {

    private RoomService roomService;

    /**
     * 根据用户名查询入住记录
     */
    @GetMapping("/username/{username}")
    public ResponseBean getRecord(@PathVariable String username,
                                  @RequestParam(name = "complete", defaultValue = "0") int complete) {

        Record record = service.getRecordsByUsername(username, complete);
        return ResponseBean.ok().data(record);
    }

    /**
     * 查询当前token下的入住记录
     */
    @GetMapping("/")
    public ResponseBean getRecordByToken(@RequestParam(name = "complete", defaultValue = "0") int complete) {
        String username = SecurityPrincipalUtil.getLoginUser().getUsername();
        Record record = service.getRecordsByUsername(username, complete);
        return ResponseBean.ok().data(record);
    }
}
