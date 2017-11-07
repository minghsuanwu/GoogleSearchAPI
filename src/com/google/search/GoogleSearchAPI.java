package com.google.search;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import com.google.gson.Gson;

public class GoogleSearchAPI {
	private static final int HTTP_REQUEST_TIMEOUT = 3 * 600000;

	public static String GOOGLE_API_KEY = "";
	public static String GOOGLE_SEARCH_ID = "";
	public static String MOBILE_SEARCH_ID = "";


	public static void search(String keyword) {
		Customsearch customsearch = new Customsearch(new NetHttpTransport(), new JacksonFactory(), 
				new HttpRequestInitializer() {
			public void initialize(HttpRequest httpRequest) {
				try {
					// set connect and read timeouts
//					httpRequest.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
//					httpRequest.setReadTimeout(HTTP_REQUEST_TIMEOUT);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		try {
			Customsearch.Cse.List list = customsearch.cse().list(keyword);
			list.setKey(GOOGLE_API_KEY);
			/*
			 * 1. Google: GOOGLE_SEARCH_ID
			 * 2. Mobile01: MOBILE_SEARCH_ID
			 */
			list.setCx(GOOGLE_SEARCH_ID);
			list.setStart(new Long(1));
			Search results = list.execute();
			List<Result> resultList = results.getItems();
			for (Result result: resultList) {
		        System.out.println(result.getDisplayLink());
		        System.out.println(result.getTitle());
				System.out.println(result.getFormattedUrl());
		        // all attributes:
//		        System.out.println(result.toString());
			}
			System.out.println("--------------------------------------");
			
			for (long i = 1; i <= 1; i++) {
				long num = i * 10;
				query(list, num);
				System.out.println("--------------------------------------");
				TimeUnit.SECONDS.sleep(2);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void query(Customsearch.Cse.List list, long num) {
		try {
			if (num == 0) num = 1;
			list.setStart(num);
			Search results = list.execute();
			List<Result> resultList = results.getItems();
			for (Result result: resultList) {
		        System.out.println(result.getDisplayLink());
		        System.out.println(result.getTitle());
				System.out.println(result.getFormattedUrl());
		        // all attributes:
//		        System.out.println(result.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String readConfig() {
		StringBuilder sb = new StringBuilder();
		String filePath = "config.txt";
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath), "UTF-8")); // read document format in "UTF-8"
			String str = null;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
		} catch (Exception e) {
			System.err.println("Please create your config.txt with your google server key in JSON format:");
			System.err.println("{\"serverkey\": \"your google server key\"}");
//			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return sb.toString();
	}

	class ConfigInfo {
		private String serverkey = "";	// GOOGLE_API_KEY
		private String GOOGLE_SEARCH_ID = "";
		private String MOBILE_SEARCH_ID = "";
		
		public String getServerkey() {
			return serverkey;
		}
		public String getGOOGLE_SEARCH_ID() {
			return GOOGLE_SEARCH_ID;
		}
		public String getMOBILE_SEARCH_ID() {
			return MOBILE_SEARCH_ID;
		}
	}
	public static String getServerKey() {
		String JSON = readConfig();
		Gson gson = new Gson();		
		ConfigInfo ci = gson.fromJson(JSON, ConfigInfo.class);
		GOOGLE_API_KEY = ci.getServerkey();
		GOOGLE_SEARCH_ID = ci.getGOOGLE_SEARCH_ID();
		MOBILE_SEARCH_ID = ci.getMOBILE_SEARCH_ID();
		
		return ci.serverkey;
	}
	
	public static void main(String[] args) {
		String keyword = "陳偉殷";
		GoogleSearchAPI.getServerKey();
		GoogleSearchAPI.search(keyword);
	}
}
