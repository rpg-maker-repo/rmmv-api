package com.trinary.rpgmaker.security.token;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Alternative;

@Alternative
public class MemoryResidentTokenManager extends TokenManager {
	protected static Map<String, Token> tokenMap = new HashMap<String, Token>();
	protected static Map<String, Token> userMap  = new HashMap<String, Token>();
	
	@Override
	protected Token getTokenByString(String tokenString) {
		synchronized(tokenMap) {
			return tokenMap.get(tokenString);
		}
	}
	
	@Override
	protected Token getTokenByPrincipal(Principal principal) {
		synchronized(userMap) {
			return userMap.get(principal.getName());
		}
	}
	
	@Override
	protected void storeToken(Token token) {
		synchronized(tokenMap) {
			tokenMap.put(token.getToken(), token);
		}
		synchronized(userMap) {
			userMap.put(token.getPrincipal().getName(), token);
		}
	}
	
	@Override
	protected Token releaseToken(Token token) {
		synchronized(tokenMap) {
			tokenMap.remove(token.getToken());
		}
		synchronized(userMap) {
			userMap.remove(token.getPrincipal().getName());
		}
		return token;
	}
}