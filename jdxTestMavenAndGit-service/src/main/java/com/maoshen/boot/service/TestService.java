package com.maoshen.boot.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {
	public String get() {
		return "jdxTest:"+System.currentTimeMillis();
	}
}
