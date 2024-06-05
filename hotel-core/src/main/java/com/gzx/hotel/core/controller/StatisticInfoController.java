package com.gzx.hotel.core.controller;

import com.gzx.hotel.base.pojo.ResponseBean;
import com.gzx.hotel.core.dto.StatisticInfoDTO;
import com.gzx.hotel.core.po.Request;
import com.gzx.hotel.core.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/statisticInfo")
@Slf4j
public class StatisticInfoController {

    @Resource
    RequestService requestService;

    @PostMapping("/room")
    public ResponseBean getStatisticInfo(@RequestBody StatisticInfoDTO statisticInfoDTO) {
        try {
            List<Request> requestList = requestService.getByRoomId(statisticInfoDTO.getRoomId(), statisticInfoDTO.getStartRequestTime(), statisticInfoDTO.getEndRequestTime());
            statisticInfoDTO.setRequestList(requestList);

            statisticInfoDTO.setUseCount(requestList.size());
            statisticInfoDTO.setTotalFee(requestList.stream().mapToDouble(Request::getFee).sum());
//        statisticInfoDTO.setMostCommonTemperature(requestList.stream().collect(Collectors.groupingBy(Request::getTemperature, Collectors.counting())).entrySet()
//                .stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null));
            statisticInfoDTO.setMostCommonTemperature(getMostCommonFromRequest(Request::getTemperature, requestList));
            statisticInfoDTO.setMostCommonWindSpeed(getMostCommonFromRequest(Request::getWindSpeed, requestList));

            return ResponseBean.ok().data(statisticInfoDTO);
        } catch (Exception e) {
            log.error("查询统计信息时发生错误, {}", e.getMessage());
            return ResponseBean.error();
        }

    }

    private <T> T getMostCommonFromRequest(Function<Request, T> function, List<Request> requestList) {
        HashMap<T, Integer> map = new HashMap<>();
        T res = null;
        int max = 0;
        for (Request request : requestList) {
            T temp = function.apply(request);
            int count = map.getOrDefault(temp, 0) + 1;
            if (count > max) {
                res = temp;
                max = count;
            }
            map.put(temp, count);
        }
        return res;
    }
}
