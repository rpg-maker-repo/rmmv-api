package com.trinary.rpgmaker.ro;

public class Link {
	protected String rel;
	protected String href;
	protected String method;
	/**
	 * @return the rel
	 */
	public String getRel() {
		return rel;
	}
	/**
	 * @param rel the rel to set
	 */
	public Link setRel(String rel) {
		this.rel = rel;
		return this;
	}
	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}
	/**
	 * @param href the href to set
	 */
	public Link setHref(String href) {
		this.href = href;
		return this;
	}
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public Link setMethod(String method) {
		this.method = method;
		return this;
	}
}