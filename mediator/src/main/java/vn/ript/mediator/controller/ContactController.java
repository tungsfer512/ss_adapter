package vn.ript.mediator.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import vn.ript.mediator.model.Organization;
import vn.ript.mediator.service.OrganizationService;
import vn.ript.mediator.service.initialize.ServiceService;
import vn.ript.mediator.utils.CustomHttpRequest;
import vn.ript.mediator.utils.CustomResponse;
import vn.ript.mediator.utils.Utils;

@RestController
@RequestMapping("/api/v1/contacts")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContactController {

    @Autowired
    OrganizationService organizationService;

    @Autowired
    ServiceService serviceService;

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> listClients() {
        try {
            String url = Utils.SS_BASE_URL + "/listClients";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Accept", "application/json"));
            CustomHttpRequest customHttpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = customHttpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(jsonResponse);
                List<Object> members = jsonObject.getJSONArray("member").toList();
                Gson gson = new Gson();

                JSONArray jsonMembers = new JSONArray();
                for (Object member : members) {
                    JSONObject jsonMember = new JSONObject(gson.toJson(member));
                    JSONObject jsonMemberId = jsonMember.getJSONObject("id");
                    String memberId = "";
                    memberId += jsonMemberId.getString("xroad_instance") + ":";
                    memberId += jsonMemberId.getString("member_class") + ":";
                    memberId += jsonMemberId.getString("member_code");
                    if (jsonMemberId.has("subsystem_code")) {
                        memberId += ":" + jsonMemberId.getString("subsystem_code");
                    }
                    Optional<Organization> checkOrganization = organizationService
                            .findBySsId(memberId);
                    if (checkOrganization.isPresent()) {
                        Organization organization = checkOrganization.get();
                        jsonMember.put("adapter_data", organization);
                    } else {
                        jsonMember.put("adapter_data", new JSONObject());
                    }
                    jsonMembers.put(jsonMember);
                }

                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonMembers.toList());
            } else {
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @PostMapping("/sendMail")
    public ResponseEntity<Map<String, Object>> sendMail(@RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("subject") ||
                    !body.containsKey("recipients") ||
                    !body.containsKey("message")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }

            String subject = (String) body.get("subject");
            Object tmp_recipients = body.get("recipients");
            String message = (String) body.get("message");

            Gson gson = new Gson();
            JSONArray jsonArray = new JSONArray(gson.toJson(tmp_recipients));
            List<Object> obj_recipients = jsonArray.toList();

            List<String> recipients =  new ArrayList<>();

            for (Object obj_recipient : obj_recipients ) {
                String recipient = obj_recipient.toString();
                recipients.add(recipient);
            }

            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setSubject(subject);
            mailMessage.setTo(recipients.toArray(new String[0]));
            mailMessage.setText(message);

            javaMailSender.send(mailMessage);
            return CustomResponse.Response_no_data(200);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

}
