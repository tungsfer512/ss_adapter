package vn.ript.ssmanageadapter.schedule;

// import java.io.IOException;
// import java.util.List;
// import java.util.Map;

// import org.apache.http.HttpResponse;
// import org.apache.http.ParseException;
// import org.apache.http.util.EntityUtils;
// import org.json.JSONArray;
// import org.json.JSONObject;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;

// import vn.ript.ssmanageadapter.utils.CustomHttpRequest;
// import vn.ript.ssmanageadapter.utils.Utils;

// @Component
public class SyncOrganization {
    // @Scheduled(fixedDelay = 1000 * 30)
    // public void SyncOrganizationEvery30Seconds() {
    //     try {
    //         System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx");
    //         Map<String, String> headers = Map.ofEntries(
    //                 Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
    //                 Map.entry("Content-Type", "application/json"),
    //                 Map.entry("Accept", "application/json"));

    //         String urlAllClient = Utils.SS_CONFIG_URL + "/clients";
    //         CustomHttpRequest httpRequestAllClient = new CustomHttpRequest("GET", urlAllClient, headers);
    //         HttpResponse httpResponseAllClient = httpRequestAllClient.request();
    //         String jsonResponseAllClient = EntityUtils.toString(httpResponseAllClient.getEntity());
    //         JSONArray jsonArrayAllClient = new JSONArray(jsonResponseAllClient);
    //         List<Object> jsonClientList = jsonArrayAllClient.toList();
    //         for (Object jsonClient : jsonClientList) {
    //             JSONObject jsonObject = new JSONObject(jsonClient);
    //             String id = jsonObject.getString("id");
    //             String url = Utils.SS_CONFIG_URL + "/clients/" + id +
    //                     "/service-client-candidates?member_name_group_description=&member_group_code=&subsystem_code=&service_client_type=SUBSYSTEM";
    //             CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
    //             HttpResponse httpResponse = httpRequest.request();
    //         }
    //     } catch (ParseException e) {
    //         e.printStackTrace();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}
