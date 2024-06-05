package com.gzx.hotel.core.controller;

import com.gzx.hotel.base.pojo.ResponseBean;
import com.gzx.hotel.core.config.SystemSettings;
import com.gzx.hotel.core.server.CentralServer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @program: hotel-parent
 * @description: 空调管理员相关控制器
 * @author: gzx
 * @create: 2024-06-05 17:59
 **/
@RestController
@RequestMapping("/temperature")
public class TemperatureController {

    @Resource
    CentralServer centralServer;

    @GetMapping("/bound")
    public ResponseBean getTempChangeRange() {
        HashMap<String, Double> tempChangeBoundMap = centralServer.getTempChangeBound();
        return ResponseBean.ok().setData(tempChangeBoundMap);
    }

    @PostMapping("/bound")
    public ResponseBean updateTempChangeRange(@RequestBody HashMap<String, Double> tempChangeBoundMap) {
        centralServer.updateTempChangeBound(tempChangeBoundMap.get("lowerBound"), tempChangeBoundMap.get("upperBound"));
        return ResponseBean.ok();
    }
}
