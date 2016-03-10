package com.maco.clientejuegos.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uclm.esi.common.jsonMessages.ErrorMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessage;
import edu.uclm.esi.common.jsonMessages.JSONMessagesBuilder;
import edu.uclm.esi.common.jsonMessages.JSONParameter;
import edu.uclm.esi.common.jsonMessages.MessageList;
import edu.uclm.esi.common.jsonMessages.OKMessage;

public class Proxy {
	public static String PREFIX = "--", LINE_END = "\r\n";

	private static Proxy yo;
	private String urlServer;

	private Proxy() {
		this.urlServer = "161.67.212.125:8080"; /*172.19.182.122*/
	}

	public static Proxy get() {
		if (yo==null)
			yo=new Proxy();
		return yo;
	}
	public JSONMessage doPost(String resourceURL, JSONMessage... jsonMessages) throws JSONException, InterruptedException, ExecutionException {
		InputStream inputStream = null;
		JSONMessage result=null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			String pars="";
			if (jsonMessages!=null && jsonMessages.length>0) {
				for (int i=0; i<jsonMessages.length; i++) {
					JSONMessage jsm=jsonMessages[i];
					if (jsm.isCommand()) {
						pars=pars+"command=" + jsm.toString();
					} else if (jsm.getType().equals(JSONParameter.class.getSimpleName())) {
						JSONParameter jsp=(JSONParameter) jsm;
						pars=pars+jsp.getName() + "=" + jsp.getValue();
					} else {
						pars=pars+jsm.getType() + "=" + jsm.toString();
					}
					pars=pars + "&";
				}
			}
			if (pars.endsWith("&"))
				pars=pars.substring(0, pars.length()-1);
			URI uri=new URI("http",
					this.urlServer,
					"/JuegosEnGrupo/" + resourceURL,
					pars,
					null);
			String sURI=uri.toASCIIString();

			HttpPost httpPost = new HttpPost(sURI);
			HttpResponse httpResponse = httpclient.execute(httpPost);
			inputStream = httpResponse.getEntity().getContent();
			if(inputStream != null)
				result = getJSONObject(inputStream);
			else
				result = new OKMessage((JSONArray) null);

		} catch (Exception e) {
			result=new ErrorMessage("Failure: " + e.getMessage());
		}
		return result;
	}

	public JSONMessage doGet(String resourceURL, JSONMessage... jsonMessages) throws JSONException, InterruptedException, ExecutionException {
		InputStream inputStream = null;
		JSONMessage result=null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			String pars="";
			if (jsonMessages!=null && jsonMessages.length>0) {
				for (int i=0; i<jsonMessages.length; i++) {
					JSONMessage jsm=jsonMessages[i];
					if (jsm.isCommand()) {
						pars=pars+"command=" + jsm.toString();
					} else if (jsm.getType().equals(JSONParameter.class.getSimpleName())) {
						JSONParameter jsp=(JSONParameter) jsm;
						pars=pars+jsp.getName() + "=" + jsp.getValue();
					} else {
						pars=pars+jsm.getType() + "=" + jsm.toString();
					}
					pars=pars + "&";
				}
			}
			if (pars.endsWith("&"))
				pars=pars.substring(0, pars.length()-1);
			URI uri=new URI("http",
					this.urlServer,
					"/JuegosEnGrupo/" + resourceURL,
					pars,
					null);
			String sURI=uri.toASCIIString();

			HttpGet httpGet = new HttpGet(sURI);
			HttpResponse httpResponse = httpclient.execute(httpGet);
			inputStream = httpResponse.getEntity().getContent();
			if(inputStream != null)
				result = getJSONObject(inputStream);
			else {
				result=new OKMessage((JSONArray) null);
			}

		} catch (Exception e) {
			result=new ErrorMessage("Failure: " + e.getMessage());
		}
		return result;
	}

	public String getUrlServer() {
		return urlServer;
	}

	private JSONMessage getJSONObject(InputStream inputStream) throws IOException, JSONException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String sResult = "";
		while((line = bufferedReader.readLine()) != null)
			sResult += line;

		inputStream.close();
		JSONObject jso=new JSONObject(sResult);
		String sResultado=jso.getString("resultado");
		jso=new JSONObject(sResultado);
		JSONMessage result= JSONMessagesBuilder.build(jso);
		return result;
	}

	public void setUrlServer(String urlServer) {
		this.urlServer = urlServer;
	}
}