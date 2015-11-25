package com.trinary.rpgmaker.security.token;

import java.security.Principal;

import javax.inject.Singleton;

@Singleton
public abstract class TokenFactory {
	public abstract Token generateToken(Principal principal);
}