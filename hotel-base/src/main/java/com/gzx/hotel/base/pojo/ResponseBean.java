package com.gzx.hotel.base.pojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;

@Data
@Accessors(chain = true)
public class ResponseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标记
	 */
	private String code = ResponseCode.SUCCESS;
	/**
	 * 提示信息
	 */
	private String msg = "操作成功";
	/**
	 * 添加，修改的实体类
	 */
	private Object data;

	public static ResponseBean ok(){
		ResponseBean result = new ResponseBean();
		result.setCode(ResponseCode.SUCCESS);
		result.setMsg("操作成功");
		result.setData(new HashMap<>());
		return result;
	}

	public static ResponseBean ok(String msg){
		ResponseBean result = new ResponseBean();
		result.setCode(ResponseCode.SUCCESS);
		result.setMsg(msg);
		result.setData(new HashMap<>());
		return result;
	}

	public static ResponseBean error(){
		ResponseBean result = new ResponseBean();
		result.setCode(ResponseCode.FAIL);
		result.setMsg("服务器内部错误，请稍后重试");
		result.setData(new HashMap<>());
		return result;
	}

	public static ResponseBean error(String msg){
		ResponseBean result = new ResponseBean();
		result.setCode(ResponseCode.FAIL);
		result.setMsg(msg);
		result.setData(new HashMap<>());
		return result;
	}

	public ResponseBean data(Object key, Object value){
		if("java.util.HashMap".equals(data.getClass().getName())) {
			((HashMap) data).put(key, value);
		}
		return this;
	}

	public ResponseBean data(Object value) {
		this.data = value;
		return this;
	}

	public String toJson() throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(this);
	}

}
