package com.tealium.net.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@BindingAnnotation
public @interface FTPFileDistributorCDNetworks {
	
	String[] tags() default "";

	String createdBy() default "Tealium Engineering";

	String lastModified() default "12/21/2014";
}
