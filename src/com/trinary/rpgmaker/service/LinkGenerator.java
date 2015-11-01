package com.trinary.rpgmaker.service;

import java.lang.reflect.Method;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.trinary.ro.Link;
import com.trinary.util.StringUtil;

@Stateless
public class LinkGenerator {
	public Link createLink(Method method, Map<String, Object> arguments) {
		String httpMethod = getHttpMethod(method);
		String href = generateLinkString(method, arguments);
		
		if (httpMethod == null || href == null) {
			return null;
		}
		
		Link link = new Link();
		link.setMethod(httpMethod);
		link.setHref(href);
		
		return link;
	}

	private String getHttpMethod(Method method) {
		if (method.getAnnotation(GET.class) != null) {
			return "GET";
		} else if (method.getAnnotation(POST.class) != null) {
			return "POST";
		} else if (method.getAnnotation(PUT.class) != null) {
			return "PUT";
		} else if (method.getAnnotation(DELETE.class) != null) {
			return "DELETE";
		} else {
			return null;
		}
	}
	
	private String generateLinkString(Method method,
			Map<String, Object> arguments) {
		Class<?> clazz = method.getDeclaringClass();
		
		Path classPathAnnotation = clazz.getAnnotation(Path.class);
		Path methodPathAnnotation = method.getAnnotation(Path.class);
		
		String pathTemplate = "";
		
		if (classPathAnnotation != null) {
			pathTemplate += classPathAnnotation.value();
		}
		
		if (methodPathAnnotation != null) {
			pathTemplate += methodPathAnnotation.value();
		}
		
		if (pathTemplate.isEmpty()) {
			return null;
		}
		
		String resourcePath = StringUtil.interpolate(pathTemplate, arguments);
//		String baseUrl = uriInfo.getBaseUri().toString();
//		
//		String url = "";
//		
//		if (resourcePath.charAt(0) == '/') {
//			url = baseUrl + resourcePath.substring(1);
//		} else {
//			url = baseUrl + resourcePath;
//		}
		
		return resourcePath;
	}
}