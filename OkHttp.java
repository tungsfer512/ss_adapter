
// System.out.println("=====================1");
// TrustManager[] trustAllCerts = new TrustManager[] {
// new X509TrustManager() {
// @Override
// public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
// String authType)
// throws CertificateException {
// }

// @Override
// public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
// String authType)
// throws CertificateException {
// }

// @Override
// public java.security.cert.X509Certificate[] getAcceptedIssuers() {
// return new java.security.cert.X509Certificate[] {};
// }
// }
// };

// // Install the all-trusting trust manager
// System.out.println("=====================2");
// final SSLContext sslContext = SSLContext.getInstance("SSL");
// sslContext.init(null, trustAllCerts, new SecureRandom());
// System.out.println("=====================3");
// // Create an ssl socket factory with our all-trusting manager
// SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
// System.out.println("=====================4");

// OkHttpClient.Builder builder = new OkHttpClient.Builder();
// System.out.println("=====================5");
// builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)
// trustAllCerts[0]);
// System.out.println("=====================6");
// builder.hostnameVerifier(new HostnameVerifier() {
// @Override
// public boolean verify(String hostname, SSLSession session) {
// return true;
// }
// });
// System.out.println("=====================7");

// OkHttpClient client = builder.build();
// System.out.println("=====================8");
// // MediaType mediaType = MediaType.parse("application/json");
// // System.out.println("=====================9");
// // JSONObject jsonObject = new JSONObject();
// // jsonObject.put("code", "CS:GOV:SS1MC:SS1SUB3");
// // jsonObject.put("pid", "");
// // jsonObject.put("name", "SS1 Subsystem 3");
// // jsonObject.put("address", "SS1 Subsystem 3 Address");
// // jsonObject.put("email", "ss1sub3@gmail.com");
// // jsonObject.put("telephone", "0123456783");
// // jsonObject.put("fax", "0123456783");
// // jsonObject.put("website", "ss1sub3.com.vn");
// // System.out.println("=====================10");

// // // RequestBody body = jsonObject.
// // RequestBody body = RequestBody.create(mediaType, jsonObject.toString());

// RequestBody requestBody = new MultipartBody.Builder()
// .setType(MultipartBody.FORM)
// .addFormDataPart("file", content.getContent().getName(),
// RequestBody.create(MediaType.parse("application/octet-stream"),content.getContent()))
// .build();

// System.out.println("=====================11");
// Request request = new Request.Builder()
// .url(url)
// .method("POST", requestBody)
// .addHeader("X-Road-Client", "CS/GOV/SS1MC/SS1SUB1")
// .addHeader("from", "CS:GOV:SS1MC:SS1SUB1")
// .addHeader("Content-Type", "application/json")
// .build();
// System.out.println("=====================12");
// Response response = client.newCall(request).execute();
// System.out.println("=====================13");

// return CustomResponse.Response_data(response.code(), response.body());