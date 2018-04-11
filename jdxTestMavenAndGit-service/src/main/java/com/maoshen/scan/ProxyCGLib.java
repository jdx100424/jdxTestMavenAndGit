package com.maoshen.scan;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class ProxyCGLib implements MethodInterceptor {
	private Enhancer enhancer = new Enhancer();

	public Object createProxy(Class<?> clazz) {
		// 设置需要创建子类的类
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);
		// 通过字节码技术动态创建子类实例
		return enhancer.create();
	}

	// 实现MethodInterceptor接口方法
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		ProxyService proxyService = (ProxyService) SpringContextUtil.getBean("proxyService");

		System.out.println("前置代理");
		// 通过代理类调用父类中的方法
		Object result = proxyService.run(method.getName(), args);
		System.out.println("后置代理");
		return result;
	}
}
