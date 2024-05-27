package com.gzx.hotel.core.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.gzx.hotel.base.controller.BaseController;
import com.gzx.hotel.base.pojo.ResponseBean;
import com.gzx.hotel.core.constant.ServerStatus;
import com.gzx.hotel.core.dto.BillDTO;
import com.gzx.hotel.core.po.Bill;
import com.gzx.hotel.core.po.Record;
import com.gzx.hotel.core.po.Request;
import com.gzx.hotel.core.po.Room;
import com.gzx.hotel.core.server.CentralServer;
import com.gzx.hotel.core.service.BillService;
import com.gzx.hotel.core.service.RecordService;
import com.gzx.hotel.core.service.RequestService;
import com.gzx.hotel.core.service.RoomService;
import com.gzx.hotel.core.utils.ExcelExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/bill")
@Slf4j
public class BillController extends BaseController<BillService, Bill> {

    @Resource
    RequestService requestService;

    @Resource
    CentralServer centralServer;

    @Resource
    ExcelExportUtil excelExportUtil;

    /**
     * 根据recordId查询所有的请求，并汇总为一份账单
     *
     * @param recordId
     * @return
     */
    @PostMapping("/record/{recordId}")
    public ResponseBean generateBill(@PathVariable Long recordId) {
        List<Request> requestList = requestService.list(Wrappers.<Request>query().eq("record_id", recordId));

        try {
            // 在统计费用之前要先将还未完成的请求完成，按照理论来说这时候请求应该是全部结束了，但是还是需要确保，否则统计的时候会失败
            finishRequests(requestList);
            AtomicReference<Double> total = new AtomicReference<>(0.0);
            requestList.forEach((request) -> total.updateAndGet(v -> v + request.getFee()));
            Bill bill = new Bill(recordId, total.get(), requestList);

            service.save(bill);
            return ResponseBean.ok().data("bill", bill);
        } catch (Exception e) {
            log.error("统计账单时出错！{}", e.getMessage());
            return ResponseBean.error();
        }
    }

    @GetMapping("/excel/{billId}")
    public void exportExcel(@PathVariable Long billId,
                            HttpServletResponse response) {
        BillDTO bill = service.getBillById(billId);
        try {
            Workbook sheets = excelExportUtil.generateBill(bill);
            // 设置Content Type
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String encodedFileName = URLEncoder.encode("空调使用账单", StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName + ".xlsx");

            // 写入到输出流
            ServletOutputStream outputStream = response.getOutputStream();
            sheets.write(outputStream);
            sheets.close();
            outputStream.close();
        } catch (Exception e) {
            log.error("导出账单excel表失败！{}", e.getMessage());
        }
    }

    @GetMapping("/detail/excel/{billId}")
    public void exportExcelDetailed(@PathVariable Long billId,
                                    HttpServletResponse response) {
        BillDTO bill = service.getBillById(billId);
        List<Request> requestList = requestService.list(Wrappers.<Request>query().eq("record_id", bill.getRecord().getId()));
        bill.setRequestList(requestList);
        try {
            Workbook sheets = excelExportUtil.generateDetail(bill);
            // 设置Content Type
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String encodedFileName = URLEncoder.encode("空调使用详单", StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName + ".xlsx");

            // 写入到输出流
            ServletOutputStream outputStream = response.getOutputStream();
            sheets.write(outputStream);
            sheets.close();
            outputStream.close();
        } catch (Exception e) {
            log.error("导出账单excel表失败！{}", e.getMessage());
        }
    }

    private void finishRequests(List<Request> requests) {
        for (Request request : requests) {
            if (!Objects.equals(request.getStatus(), ServerStatus.END)) {
                centralServer.finishRequest(request);
                requestService.updateById(request);
            }
        }
    }

}
