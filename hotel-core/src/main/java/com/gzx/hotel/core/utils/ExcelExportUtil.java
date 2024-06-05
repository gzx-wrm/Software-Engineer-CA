package com.gzx.hotel.core.utils;

import com.gzx.hotel.core.constant.Wind;
import com.gzx.hotel.core.dao.BillDao;
import com.gzx.hotel.core.dto.BillDTO;
import com.gzx.hotel.core.po.Bill;
import com.gzx.hotel.core.po.Record;
import com.gzx.hotel.core.po.Request;
import com.gzx.hotel.core.po.Room;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Component
public class ExcelExportUtil {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public <T> Workbook generateExcelWorkBook(List<T> objList, Class<T> clazz) throws IOException, IllegalAccessException {
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建表单
        Sheet sheet = workbook.createSheet(clazz.getSimpleName());

        // 创建表头
        Row headerRow = sheet.createRow(0);
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            headerRow.createCell(i).setCellValue(fields[i].getName());
        }

        // 填充数据
        int rowNum = 1;
        for (T obj : objList) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object value = fields[i].get(obj);
                if (value != null) {
                    row.createCell(i).setCellValue(value.toString());
                } else {
                    row.createCell(i).setCellValue("");
                }
            }

        }

        return workbook;
//        // 设置Content Type
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");
//
//        // 写入到输出流
//        ServletOutputStream outputStream = response.getOutputStream();
//        workbook.write(outputStream);
//        workbook.close();
//        outputStream.close();
    }

    public Workbook generateBill(BillDTO bill) {
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建表单
        Sheet sheet = workbook.createSheet("账单表");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("房间号");
        headerRow.createCell(1).setCellValue("入住时间");
        headerRow.createCell(2).setCellValue("退房时间");
        headerRow.createCell(3).setCellValue("空调总费用(元)");

        // 填充数据
        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(bill.getRoomName());
        row.createCell(1).setCellValue(sdf.format(bill.getRecord().getStartTime()));
        row.createCell(2).setCellValue(sdf.format(bill.getRecord().getEndTime()));
        row.createCell(3).setCellValue(bill.getTotalFee());

        return workbook;
    }

    public Workbook generateDetail(BillDTO bill) {
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建表单
        Sheet sheet = workbook.createSheet("详单表");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("房间号");
        headerRow.createCell(1).setCellValue("请求时间");
        headerRow.createCell(2).setCellValue("服务开始时间");
        headerRow.createCell(3).setCellValue("服务结束时间");
        headerRow.createCell(4).setCellValue("服务时长");
        headerRow.createCell(5).setCellValue("风速");
        headerRow.createCell(6).setCellValue("费用(元)");
        headerRow.createCell(7).setCellValue("费率(元/秒)");

        // 填充数据
        int rowNum = 1;
        for (Request request : bill.getRequestList()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(bill.getRoomName());
            row.createCell(1).setCellValue(sdf.format(request.getRequestTime()));
            row.createCell(2).setCellValue(sdf.format(request.getStartTime()));
            row.createCell(3).setCellValue(sdf.format(request.getEndTime()));

            // 计算两个日期之间的差值
            Duration duration = Duration.between(request.getStartTime().toInstant(), request.getEndTime().toInstant());

            // 获取小时、分钟和秒
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            long seconds = duration.getSeconds() % 60;
            row.createCell(4).setCellValue(hours + " 小时 " + minutes + " 分钟 " + seconds + " 秒");
            row.createCell(5).setCellValue(Objects.equals(request.getWindSpeed(), Wind.HIGH) ? "高风速" : (request.getWindSpeed().equals(Wind.MIDDLE) ? "中风速" : "低风速"));
            row.createCell(6).setCellValue(request.getFee());
            row.createCell(7).setCellValue(request.getRate());
        }

        return workbook;
    }
}

