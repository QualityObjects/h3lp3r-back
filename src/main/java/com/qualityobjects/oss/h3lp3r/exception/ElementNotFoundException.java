package com.qualityobjects.oss.h3lp3r.exception;

public class ElementNotFoundException extends QOException {

	private static final long serialVersionUID = 5207795630755628143L;

	public ElementNotFoundException(String entity, Object id) {
		super(ErrorCodes.ELEMENT_NOT_FOUND, 
				String.format("No se encontró un elemento de '%s' con ID: %s", entity, id), 
				createMap("entity", entity, "id", id));
	} 
	
	public ElementNotFoundException(String msg) {
		super(ErrorCodes.ELEMENT_NOT_FOUND, msg);
	}

}
