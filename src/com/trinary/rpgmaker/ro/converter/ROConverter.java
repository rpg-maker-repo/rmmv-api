package com.trinary.rpgmaker.ro.converter;

import java.util.ArrayList;
import java.util.List;

public abstract class ROConverter<RO, ENTITY> {
	protected abstract ENTITY _convertRO(RO ro);
	protected abstract RO _convertEntity(ENTITY entity);
	protected abstract RO _addLinks(RO object);
	
	public RO convertEntity(ENTITY entity) {
		if (entity == null) {
			return null;
		}
		
		RO ro = _convertEntity(entity);
		RO roWithLinks = _addLinks(ro);
		
		if (roWithLinks == null) {
			return ro;
		}
		
		return roWithLinks;
	}
	
	public ENTITY convertRO(RO ro) {
		if (ro == null) {
			return null;
		}
		
		return _convertRO(ro);
	}
	
	public List<RO> convertEntityList(List<ENTITY> entityList) {
		if (entityList == null) {
			return null;
		}
		
		List<RO> roList = new ArrayList<RO>();
		for (ENTITY entity : entityList) {
			roList.add(convertEntity(entity));
		}
		return roList;
	}
	
	public List<ENTITY> convertROList(List<RO> roList) {
		if (roList == null) {
			return null;
		}
		
		List<ENTITY> entityList = new ArrayList<ENTITY>();
		for (RO ro : roList) {
			entityList.add(convertRO(ro));
		}
		return entityList;
	}
}