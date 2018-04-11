package com.maoshen.scan;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MyComponent {
	String value() default "";
}
