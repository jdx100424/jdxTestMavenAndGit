package com.maoshen.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.maoshen.boot.service.TestService;

@Controller
public class SampleController {
	@Autowired
	private TestService testService;

	@RequestMapping("/")
	@ResponseBody
	public String home() {
		return testService.get();
	}
}