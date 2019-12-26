package com.indicosmic.www.mypolicynow_sdk.webservices;

public class RestClient
	{
		private static API REST_CLIENT;

		//public static String ROOT_URL = "https://www.mypolicynow.com/Api/";

		public static String ROOT_URL = "http://uat.mypolicynow.com/ags/";
		public static String URLProd = "https://www.mypolicynow.com/Api/";
		public  static String ROOT_URL_BREAKIN = "http://uat.mypolicynow.com/Api/";
		public static String URLUAT = "http://uat.mypolicynow.com/";

		public static String GenerateQrCode = "https://pierre2106j-qrcode.p.rapidapi.com/api?backcolor=ffffff&pixel=10&ecl=L+%7C+M%7C+Q+%7C+H&forecolor=000000&type=json+%7C+url+%7C+tel+%7C+sms+%7C+email&";


		public static String Development = "http://www.mymfnow.com/api";
		/*static
			{
				setupRestClient();
			}*/


		private RestClient()
			{
			}

		public static API get()
			{
				return REST_CLIENT;
			}

		/*private static void setupRestClient()
			{
				RestAdapter.Builder builder = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(ROOT).setClient(new OkClient(new OkHttpClient()));
				RestAdapter restAdapter = builder.build();
				REST_CLIENT = restAdapter.create(API.class);
			}*/
	}
