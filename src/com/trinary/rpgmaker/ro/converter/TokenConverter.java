package com.trinary.rpgmaker.ro.converter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Singleton;

import com.trinary.ro.converter.ROConverter;
import com.trinary.rpgmaker.persistence.entity.User;
import com.trinary.rpgmaker.resource.TokenResource;
import com.trinary.rpgmaker.ro.TokenRO;
import com.trinary.rpgmaker.security.token.Token;
import com.trinary.rpgmaker.service.LinkGenerator;

@Singleton
public class TokenConverter extends ROConverter<TokenRO, Token> {
	@EJB LinkGenerator generator;
	
	@Override
	protected Token _convertRO(TokenRO ro) {
		Token token = new Token();
		token.setToken(ro.getToken());
		token.setExpires(ro.getExpires());
		
		return token;
	}

	@Override
	protected TokenRO _convertEntity(Token entity) {
		User user = (User)entity.getPrincipal();
		
		TokenRO ro = new TokenRO();
		ro.setPrincipal(user.getUsername());
		ro.setToken(entity.getToken());
		ro.setExpires(entity.getExpires());
		ro.setRoles(user.getRoleNames());
		
		return ro;
	}

	@Override
	protected TokenRO _addLinks(TokenRO object) {
		try {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("tokenString", object.getToken());
			Method releaseToken = TokenResource.class.getMethod("releaseToken", String.class);
			object.addLink(generator.createLink(releaseToken, args).setRel("release"));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
		
		return object;
	}
}