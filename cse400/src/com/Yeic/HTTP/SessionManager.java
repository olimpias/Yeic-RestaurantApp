package com.Yeic.HTTP;


import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class SessionManager {
	private static DefaultHttpClient httpClient=new DefaultHttpClient();
	private static HttpContext localContext=new BasicHttpContext();
	private static CookieStore cookie=new BasicCookieStore();
	
	public static CookieStore getCookie() {
		return cookie;
	}public static DefaultHttpClient getHttpClient() {
		return httpClient;
	}public static HttpContext getLocalContext() {
		return localContext;
	}
	/**
	 * 
	 * Initialize cookies on httpContext.
	 * @pre localContext!=null
	 */
	public static void inilizeCookie(){
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookie);
	}
	/**
	 * list cookies on http.Using on debug is suggested.
	 */
	public static void setCookies(){
		for(int i=0;i<cookie.getCookies().size();i++)
		httpClient.getCookieStore().addCookie(cookie.getCookies().get(i));
	}
	/**
	 * 
	 * logOut() delete all user information on http and cookies.
	 * @pre httpClient!=null
	 */
	public static void logOut(){
		httpClient.getConnectionManager().shutdown();
		httpClient=new DefaultHttpClient();
		localContext=new BasicHttpContext();
		cookie=new BasicCookieStore();
	}
}
