package com.vidya.utils;

public enum HTTPMethod
{
	GET(1), POST(2);

	private int httpMethod;

	private HTTPMethod(int method)
	{
		this.httpMethod = method;
	}

	public int getHTTPMethod()
	{
		return httpMethod;
	}
}