package com.maoshen.scan;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import com.alibaba.fastjson.JSONObject;

public class ProxyScanner implements BeanFactoryPostProcessor {
	private String basePackages;
	private Map<String, Class<?>> proxyMap = new HashMap<String, Class<?>>();
	private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();
	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();

	public String getBasePackages() {
		return basePackages;
	}

	public void setBasePackages(String basePackages) {
		this.basePackages = basePackages;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		scan(basePackages);
		scanMap(beanFactory);

		// 单独扫描指定 service
		ProxyService proxyService = new ProxyService();
		beanFactory.registerSingleton("proxyService", proxyService);
	}

	private void scanMap(ConfigurableListableBeanFactory beanFactory) {
		proxyMap.forEach((k, v) -> {
			Object proxyObject = new ProxyCGLib().createProxy(v);
			beanFactory.registerSingleton(k, proxyObject);
		});
	}

	private void scan(String basePackage) {
		includeFilters.add(new AnnotationTypeFilter(MyComponent.class));
		try {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ resolveBasePackage(basePackage) + '/' + "**/*.class";
			Resource[] resources = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					try {
						MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
						if (isCandidateComponent(metadataReader)) {
							ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
							sbd.setResource(resource);
							sbd.setSource(resource);
							if (isCandidateComponent(sbd)) {

							} else {
								// doing
								Class<?> c = Class.forName(sbd.getBeanClassName());
								Map<String,Object> map = sbd.getMetadata().getAnnotationAttributes("com.maoshen.scan.MyComponent");
								Object value = map.get("value");
								if(value == null || StringUtils.isBlank(value.toString())) {
									String arr [] = sbd.getBeanClassName().split("\\.");
									if(arr!=null && arr.length>0) {
										String s = arr[arr.length-1];
										value = s.substring(0, 1).toLowerCase() + s.substring(1);
									}
								}
								proxyMap.put(value.toString(), c);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String resolveBasePackage(String basePackage) {
		return ClassUtils
				.convertClassNameToResourcePath(new StandardEnvironment().resolveRequiredPlaceholders(basePackage));
	}

	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		AnnotationMetadata metadata = beanDefinition.getMetadata();
		return (metadata.isIndependent() && (metadata.isConcrete()
				|| (metadata.isAbstract() && metadata.hasAnnotatedMethods(Lookup.class.getName()))));
	}

	protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
		for (TypeFilter tf : this.includeFilters) {
			if (tf.match(metadataReader, this.metadataReaderFactory)) {
				return true;
			}
		}
		return false;
	}
}
