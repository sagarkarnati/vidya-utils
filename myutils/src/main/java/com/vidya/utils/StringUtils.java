package com.vidya.utils;

/**
 * @author Vidya Sagar
 * 
 */
public enum StringUtils
{
	INSTANCE;

	public boolean isValid(String str)
	{
		return (str != null && str.trim().length() > 0);
	}
}
