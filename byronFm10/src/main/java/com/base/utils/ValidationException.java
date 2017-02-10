package com.base.utils;

/**
 * <p>
 * Validation exception.
 * </p>
 * 
 * @author <a href="mailto:sunyi4j@gmail.com">Roy</a> on Sep 2, 2011
 */
public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ValidationException(String exceptionMsg) {
		super(exceptionMsg);
    }

	public ValidationException(String exceptionMsg, Throwable throwable) {
		super(exceptionMsg, throwable);
    }

}