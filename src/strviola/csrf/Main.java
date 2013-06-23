package strviola.csrf;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;

public class Main {
	public static void main(String[] args) {
		Connect.HttpGetAndParam formRequest =
				new Connect.HttpGetAndParam(Connect.localhost + "/form/");
		Connect.HttpPostAndParam postRequest =
				new Connect.HttpPostAndParam(Connect.localhost + "/form/post/");
		try {
			String response = formRequest.execute();
			System.out.println(response);

			// post to board
			postRequest.addParameter("title", "From Java");
			postRequest.addParameter("body", "Hello CSRF 2");
			postRequest.addParameter("csrfmiddlewaretoken",
					Connect.getCookieByName("csrftoken")); // with csrf token
			String postResponse = postRequest.execute();
			System.out.println(postResponse);

		} catch (ClientProtocolException e) {
			System.out.println("Bad Protocol");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
