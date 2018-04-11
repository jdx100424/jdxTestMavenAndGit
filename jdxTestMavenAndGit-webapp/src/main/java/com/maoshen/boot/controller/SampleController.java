package com.maoshen.boot.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.maoshen.boot.service.TestService;
import com.maoshen.scan.TestProxy;
import com.maoshen.scan.TestProxy2;

@Controller
public class SampleController {
	@Autowired
	private TestService testService;
	@Autowired
	private TestProxy jdxTestProxy;
	@Autowired
	private TestProxy2 testProxy2;

	@RequestMapping("/")
	@ResponseBody
	public String home() {
		String str = jdxTestProxy.jdx();
		String maoshen = testProxy2.maoshen();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("testService.get()", testService.get());
		map.put("jdxTestProxy.jdx()",str);
		map.put("testProxy2.maoshen()",maoshen);
		return JSONObject.toJSONString(map);
	}
}