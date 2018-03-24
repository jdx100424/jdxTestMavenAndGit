package com.maoshen.base;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
public class ErrorHandleController implements ErrorController {

	@Override
	public String getErrorPath() {
		return "/error";
	}

	@RequestMapping
	@ResponseBody
	public String errorHandle() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", 404);
		map.put("message", "找不到路径");
		return JSONObject.toJSONString(map);
	}
}
