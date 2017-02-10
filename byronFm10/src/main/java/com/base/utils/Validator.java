package com.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.base.utils.ValidationException;

/**
 * <p>
 * Validation utility class.
 * </p>
 * 
 * @author <a href="mailto:sunyi4j@gmail.com">Roy</a> on Sep 2, 2011
 */
public class Validator {


	/**
	 * Validate string by specified ValidationType. Support variables replacing
	 * in message.
	 * 
	 * @param type ValidationType
	 * @param str
	 * @param msg error message
	 */
	public static void validate(String rule, String str, String msg) throws ValidationException{
		Pattern pattern = Pattern.compile(rule);
		Matcher matcher = pattern.matcher(str);
		// Validation fail, throw exception
		if (!matcher.matches()) {
			throw new ValidationException(msg);
		}
	}

}