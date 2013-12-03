package com.vidya.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author Vidya Sagar
 * 
 */
public enum RESTUtils
{
	INSTANCE;

	private RESTUtils()
	{
		getSslEnabled();
	}

	public String sendHttpRequest(String requestUrl, HTTPMethod method, Map<String, String> requestparams, int connectionTimeout, int readTimeout) throws Exception
	{
		String requestParams = extractParams(requestparams);

		URL url;
		if (method == HTTPMethod.GET)
			url = new URL(requestUrl + requestParams);
		else
			url = new URL(requestUrl);

		URLConnection urlConn = url.openConnection();
		urlConn.setUseCaches(false);
		urlConn.setReadTimeout(readTimeout);
		urlConn.setConnectTimeout(connectionTimeout);

		// the request will return a response
		urlConn.setDoInput(true);
		if (method == HTTPMethod.POST)
		{
			// set request method to POST
			urlConn.setDoOutput(true);
			if (CollectionUtils.INSTANCE.hasElements(requestparams))
			{
				OutputStreamWriter writer = new OutputStreamWriter(urlConn.getOutputStream());
				writer.write(requestParams);
				writer.flush();
			}
		} else if (method == HTTPMethod.GET)
		{
			// set request method to GET
			urlConn.setDoOutput(false);
		} else
		{
			throw new Exception("HTTP Method not supported");
		}

		// reads response, stores in a file
		BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = reader.readLine()) != null)
		{
			buffer.append(line);
		}
		reader.close();

		return buffer.toString();
	}

	private String extractParams(Map<String, String> requestparams)
	{
		StringBuilder paramBuilder = new StringBuilder("&");

		for (Entry<String, String> entry : requestparams.entrySet())
		{
			paramBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("?");
		}

		return paramBuilder.toString().substring(0, paramBuilder.toString().length() - 1);
	}

	public String sendGetRequest(String url) throws Exception
	{
		return sendHttpRequest(url, HTTPMethod.GET, Collections.<String, String> emptyMap(), 5000, 5000);
	}

	public String sendGetRequest(String url, String proxyIp, long proxyPort) throws Exception
	{
		System.setProperty("http.proxyHost", proxyIp);
		System.setProperty("http.proxyPort", proxyPort + "");

		return sendHttpRequest(url, HTTPMethod.GET, Collections.<String, String> emptyMap(), 5000, 5000);
	}

	public String sendPostRequest(String url) throws Exception
	{
		return sendHttpRequest(url, HTTPMethod.POST, Collections.<String, String> emptyMap(), 5000, 5000);
	}

	public String sendPostRequest(String url, String proxyIp, long proxyPort) throws Exception
	{
		System.setProperty("http.proxyHost", proxyIp);
		System.setProperty("http.proxyPort", proxyPort + "");

		return sendHttpRequest(url, HTTPMethod.POST, Collections.<String, String> emptyMap(), 5000, 5000);
	}

	public void getSslEnabled()
	{
		try
		{
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
			{

				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws CertificateException
				{

				}

				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws CertificateException
				{

				}

				public java.security.cert.X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}
			} };
			SSLContext sc = SSLContext.getInstance("SSL");
			// Create empty HostnameVerifier
			HostnameVerifier hv = new HostnameVerifier()
			{
				public boolean verify(String arg0, SSLSession arg1)
				{
					return true;
				}
			};

			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		} catch (Exception e)
		{
			System.err.println("Exception in setting SSL Properties : Exception - " + e);
		}
	}
}