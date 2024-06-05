package com.gzx.hotel.core.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.gzx.hotel.base.po.BaseEntity;
import com.gzx.hotel.core.utils.DecimalFormatUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

@Data
public class Request extends BaseEntity {

    private Long recordId;

    @TableField(exist = false)
    private Long roomId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date requestTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    private Double temperature;

    @TableField("windspeed")
    private Integer windSpeed;

    private Double fee;

    private Double rate;

    private Integer status;

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Request && this.getId().equals(((Request) obj).getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public void calRate(Double feeRate, Double powerConsumption) {
        rate = DecimalFormatUtil.format(feeRate * powerConsumption, 2);
    }

    public void calFee() {
        fee = DecimalFormatUtil.format(((double) ((int) (endTime.getTime() - startTime.getTime()))) / 1000 * rate, 2);
    }

    public static void main(String[] args) {
        Request request1 = new Request();
        request1.setRecordId(1L);

        Request request2 = new Request();
        request2.setRecordId(1L);

        HashMap<Request, Integer> map = new HashMap<>();
        map.put(request1, 1);
        map.put(request2, 2);

        System.out.println(map.get(request1) + map.get(request2));
    }
}
