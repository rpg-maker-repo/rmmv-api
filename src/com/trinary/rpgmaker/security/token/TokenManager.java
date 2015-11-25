package com.trinary.rpgmaker.security.token;

import java.security.Principal;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public abstract class TokenManager {
	@Inject TokenFactory tokenFactory;
	
	protected abstract Token getTokenByString(String tokenString);
	protected abstract Token getTokenByPrincipal(Principal principal);
	protected abstract void storeToken(Token token);
	protected abstract Token releaseToken(Token token);
	
	public Token createToken(Principal principal) {
		Token token = getTokenByPrincipal(principal);
		
		if (token == null || token.isExpired()) {
			token = tokenFactory.generateToken(principal);
		}
		
		storeToken(token);
		
		return token;
	}
	
	public Token authenticateToken(String tokenString) throws TokenInvalidException, TokenExpiredException {
		Token token = getTokenByString(tokenString);
		
		if (token == null) {
			throw new TokenInvalidException("Access is denied.  Token is invalid.");
		}
		
		if (token.isExpired()){
			throw new TokenExpiredException("Access is denied.  Token is expired.");
		}
		
		return token;
	}
	
	public Token releaseToken(String tokenString) {
		Token token = getTokenByString(tokenString);
		
		if (token == null) {
			return null;
		}
		
		return releaseToken(token);
	}
}