package com.maoshen.scan;

import org.apache.commons.lang.StringUtils;

public class ProxyService {
	public String run(String method,Object[] args) {
		if (StringUtils.isBlank(method)) {
			System.out.println("method is null");
			return "method is null";
		}

		else if (method.equals("jdx")) {
			System.out.println("method is jdx");
			return "method is jdx";
		}

		else if (method.equals("maoshen")) {
			System.out.println("method is maoshen");
			return "method is maoshen";
		} else {
			System.out.println("method is unknown");
			return "method is unknown";
		}
	}

}
