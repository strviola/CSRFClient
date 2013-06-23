package strviola.csrf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class Connect {
	public static final String localhost = "http://127.0.0.1:8000";
	public static final String yahoo = "http://www.yahoo.co.jp";
	private static CookieStore cookie = new BasicCookieStore();
	private static HttpContext ctx = new BasicHttpContext();
	
	public static class HttpGetAndParam {
		private String param;
		private String url;

		public HttpGetAndParam(String url) {
			this.url = url;
		}

		public void addParameter(String key, String value) {
			if (param == null) {
				param = "?";
			} else {
				param += "&";
			}
			// form becomes like "?key1=value1&key2=value2"
			param += (key + "=" + value);
		}

		public String execute() throws ClientProtocolException, IOException {
			if (param == null) {
				param = "";
			}
			HttpGet request = new HttpGet(url + param);
			return httpConnect(request);
		}
	}

	public static String httpGet(String url)
			throws ClientProtocolException, IOException {
		HttpGet getRequest = new HttpGet(url);
		return httpConnect(getRequest);
	}

	/**
	 * My HTTP Post class for utility
	 * @author SuzukiRyota
	 */
	public static class HttpPostAndParam extends HttpPost {
		private ArrayList<BasicNameValuePair> postParams;
		
		public HttpPostAndParam(String url) {
			super(url);
			this.postParams = new ArrayList<BasicNameValuePair>();
		}
		
		public void addParameter(String key, String value) {
			postParams.add(new BasicNameValuePair(key, value));
		}
		
		public String execute() throws ClientProtocolException, IOException {
			this.setEntity(new UrlEncodedFormEntity(postParams));
			return httpConnect(this);
		}
	}

	private static String httpConnect(HttpRequestBase request)
			throws ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		// use cookie
		ctx.setAttribute(ClientContext.COOKIE_STORE, cookie);
		String response =
				client.execute(request, new ResponseHandler<String>() {
					@Override
					public String handleResponse(HttpResponse response)
							throws ClientProtocolException, IOException {
						if (response.getStatusLine().getStatusCode() ==
								HttpStatus.SC_OK) {
							return EntityUtils.toString(
									response.getEntity(), "utf-8");
						} else {
							return response.getStatusLine().toString();
						}
					}
				}, ctx);
		return response;
	}

	public static HashMap<String, String> getCookies() {
		HashMap<String, String> cookies = new HashMap<String, String>();
		for (Cookie c: cookie.getCookies()) {
			System.out.println(c.getName() + "=" + c.getValue());
			cookies.put(c.getName(), c.getValue());
		}
		return cookies;
	}

	public static String getCookieByName(String key) {
		for (Cookie c: cookie.getCookies()) {
			if (c.getName().equals(key)) {
				return c.getValue();
			}
		}
		// cookie not found
		return null;
	}

}
