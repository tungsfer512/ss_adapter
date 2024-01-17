package vn.ript.csadapter.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import vn.ript.csadapter.utils.CustomHttpRequest;
import vn.ript.csadapter.utils.Utils;

@Component
public class SyncOrganization {

    @Scheduled(fixedDelay = 1000 * 30)
    public void SyncOrganizationEvery30Seconds() {
        // try {
            
        // } catch (ParseException e) {
        //     e.printStackTrace();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }
}
