package com.fwrestnet;

/**
 * 
 *@comments 网络访问异常
 */
public class NetworkException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public NetworkException(String exceptionMsg) {
		super(exceptionMsg);
    }

	public NetworkException(String exceptionMsg, Throwable throwable) {
		super(exceptionMsg, throwable);
    }


}