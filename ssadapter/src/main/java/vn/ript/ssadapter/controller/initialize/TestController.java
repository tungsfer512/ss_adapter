package vn.ript.ssadapter.controller.initialize;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/test")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TestController {

    @GetMapping("")
    public ResponseEntity<InputStreamResource> getCSRS() {
        try {
            String url = Utils.SS_CONFIG_URL
                    + "/keys/5D2482D66989ED47278446B5CF666E04F1C53021/csrs/64AA9956713DC7E177FE1F4DAFAC5480C11F29A8?csr_format=PEM";
            String filename = "sign_csr_20231205_member_CS_GOV_MANAGESS2MC.der";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY));
            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStreamResource inputStreamResource = new InputStreamResource(httpEntity.getContent());
                return CustomResponse.Response_file(200, inputStreamResource, filename);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
