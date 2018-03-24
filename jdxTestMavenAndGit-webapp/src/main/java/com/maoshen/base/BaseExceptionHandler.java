package com.maoshen.base;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
class BaseExceptionHandler {
	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public Map<String, Object> defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", 500);
		map.put("message", "系统发生错误");
		return map;
	}
}
