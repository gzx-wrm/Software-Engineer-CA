package com.gzx.hotel.core.server;

import com.gzx.hotel.core.config.SystemSettings;
import com.gzx.hotel.core.constant.ServerStatus;
import com.gzx.hotel.core.po.Request;

import java.util.Date;
import java.util.Objects;

public class ServerObject implements Comparable<ServerObject>{

    private Request request;

    public ServerObject(Request request) {
        this.request = request;
    }

    public ServerObject() {
    }

    public void finish() {
        if (Objects.equals(request.getStatus(), ServerStatus.RUNNING)) {
            endTiming();
            afterFinish();
        }
    }

    public Integer getWindSpeed() {
        return request.getWindSpeed();
    }

    public Date getStartTime() {
        return request.getStartTime();
    }

    public Long getRequestId() {
        return request.getId();
    }

    public Request getRequest(){
        return request;
    }

    public void start() {
        startTiming();
    }

    private void startTiming() {
        request.setStatus(ServerStatus.RUNNING);
        request.setStartTime(new Date());
    }

    private void endTiming() {
        request.setEndTime(new Date());
        request.setStatus(ServerStatus.END);
    }

    public void afterFinish() {
        request.calRate(SystemSettings.getFeeRate(), SystemSettings.getPowerConsumptionRate(request.getWindSpeed()));
        request.calFee();
    }

    @Override
    public int compareTo(ServerObject o) {
        if (Objects.equals(request.getWindSpeed(), o.getWindSpeed())) {
            return this.getStartTime().compareTo(o.getStartTime());
        }
        return this.getWindSpeed() - o.getWindSpeed();
    }
}
