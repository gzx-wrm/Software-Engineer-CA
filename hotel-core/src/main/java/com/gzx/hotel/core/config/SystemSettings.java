package com.gzx.hotel.core.config;

import com.gzx.hotel.core.constant.Wind;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;

/**
 * 系统配置读取类
 */
@Configuration
@PropertySource("classpath:application.yml")
@Data
public class SystemSettings {

    private static Integer serverAbility;

    private static Double feeRate;

    private static Long waitingTime;

    private static HashMap<Integer, Double> tempChangeRule;

    private static HashMap<Integer, Double> powerConsumptionRule;

    @Autowired
    public SystemSettings(@Value("${central-airconditioner.ability}") String serverAbility,
                          @Value("${central-airconditioner.fee-rate}") String feeRate,
                          @Value("${central-airconditioner.server-waiting-time}") String waitingTime,
                          @Value("${central-airconditioner.temperature-change-rate.high}") String TCRHigh,
                          @Value("${central-airconditioner.temperature-change-rate.middle}") String TCRMiddle,
                          @Value("${central-airconditioner.temperature-change-rate.low}") String TCRLow,
                          @Value("${central-airconditioner.power-consumption-rate.high}") String PCRHigh,
                          @Value("${central-airconditioner.power-consumption-rate.middle}") String PCRMiddle,
                          @Value("${central-airconditioner.power-consumption-rate.low}") String PCRLow) {

        SystemSettings.serverAbility = Integer.parseInt(serverAbility);
        SystemSettings.feeRate = Double.parseDouble(feeRate);
        SystemSettings.waitingTime = Long.parseLong(waitingTime);
        initPowerConsumptionRule(PCRHigh, PCRMiddle, PCRLow);
        initTempChangeRule(TCRHigh, TCRMiddle, TCRLow);
    }

    private void initTempChangeRule(String TCRHigh, String TCRMiddle, String TCRLow) {
        tempChangeRule = new HashMap<>();
        tempChangeRule.put(Wind.HIGH, Double.parseDouble(TCRHigh));
        tempChangeRule.put(Wind.MIDDLE, Double.parseDouble(TCRMiddle));
        tempChangeRule.put(Wind.LOW, Double.parseDouble(TCRLow));
    }

    private void initPowerConsumptionRule(String PCRHigh, String PCRMiddle, String PCRLow) {
        powerConsumptionRule = new HashMap<>();
        powerConsumptionRule.put(Wind.HIGH, 1/Double.parseDouble(PCRHigh));
        powerConsumptionRule.put(Wind.MIDDLE, 1/Double.parseDouble(PCRMiddle));
        powerConsumptionRule.put(Wind.LOW, 1/Double.parseDouble(PCRLow));
    }

    public static Double getPowerConsumptionRate(Integer windSpeed) {
        return powerConsumptionRule.get(windSpeed);
    }

    public static Double getTempChangeRate(Integer windSpeed) {
        return tempChangeRule.get(windSpeed);
    }

    public static Long getWaitingTime() {
        return waitingTime;
    }

    public static Double getFeeRate() {
        return feeRate;
    }

    public static Integer getServerAbility() {
        return serverAbility;
    }

}
