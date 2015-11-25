package com.trinary.rpgmaker.security.token;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.text.ParseException;
import java.util.UUID;

import javax.enterprise.inject.Alternative;

import org.apache.commons.codec.binary.Base64;

@Alternative
public class SHA1TokenFactory extends TokenFactory {

	@Override
	public Token generateToken(Principal principal) {
		MessageDigest digest;
		String uuid = UUID.randomUUID().toString();
		
		try {
			digest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		
		digest.update(String.format("%d:%s", principal.hashCode(), uuid).getBytes());
		
		Token token = new Token();
		token.setPrincipal(principal);
		token.setToken(Base64.encodeBase64String(digest.digest()));
		try {
			token.setExpires(TimeUtils.getLaterDate("24 hours"));
		} catch (ParseException e) {
			return null;
		}
		
		return token;
	}

}