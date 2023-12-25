
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
// final SSLContext sslContext = SSLContext.getInstance("SSL");
// sslContext.init(null, trustAllCerts, new SecureRandom());
// // Create an ssl socket factory with our all-trusting manager
// SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

// OkHttpClient.Builder builder = new OkHttpClient.Builder();
// builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)
// trustAllCerts[0]);
// builder.hostnameVerifier(new HostnameVerifier() {
// @Override
// public boolean verify(String hostname, SSLSession session) {
// return true;
// }
// });

// OkHttpClient client = builder.build();
// // MediaType mediaType = MediaType.parse("application/json");
// // JSONObject jsonObject = new JSONObject();
// // jsonObject.put("code", "CS:GOV:SS1MC:SS1SUB3");
// // jsonObject.put("pid", "");
// // jsonObject.put("name", "SS1 Subsystem 3");
// // jsonObject.put("address", "SS1 Subsystem 3 Address");
// // jsonObject.put("email", "ss1sub3@gmail.com");
// // jsonObject.put("telephone", "0123456783");
// // jsonObject.put("fax", "0123456783");
// // jsonObject.put("website", "ss1sub3.com.vn");

// // // RequestBody body = jsonObject.
// // RequestBody body = RequestBody.create(mediaType, jsonObject.toString());

// RequestBody requestBody = new MultipartBody.Builder()
// .setType(MultipartBody.FORM)
// .addFormDataPart("file", content.getContent().getName(),
// RequestBody.create(MediaType.parse("application/octet-stream"),content.getContent()))
// .build();

// Request request = new Request.Builder()
// .url(url)
// .method("POST", requestBody)
// .addHeader("X-Road-Client", "CS/GOV/SS1MC/SS1SUB1")
// .addHeader("from", "CS:GOV:SS1MC:SS1SUB1")
// .addHeader("Content-Type", "application/json")
// .build();
// Response response = client.newCall(request).execute();

// return CustomResponse.Response_data(response.code(), response.body());