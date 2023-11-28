package vn.ript.ssadapter.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.util.ResourceUtils;

import com.google.common.hash.Hashing;

public class Utils {

    public static String EDocDir = System.getenv("API_WORK_DIR") + "/src/main/resources/edocs/";
    public static String uploadDir = System.getenv("API_WORK_DIR") + "/src/main/resources/attachments/";
    public static String SS_IP = System.getenv("SS_IP") != null ? System.getenv("SS_IP") : "localhost";
    public static String SS_ID = System.getenv("SS_ID") != null ? System.getenv("SS_ID") : "";

    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    public static String encodeToBase64(String file_path) {
        try {
            File file = ResourceUtils.getFile(file_path);
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new IllegalStateException("could not read file in " + file_path, e);
        }
    }

    public static String encodeToBase64(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new IllegalStateException("could not read file in " + file.toPath(), e);
        }
    }

    public static String encodeToBase64(byte[] fileContent) {
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static String datetime_now() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now).toString();
    }
    
    public static String date_now() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now).toString();
    }
    
    public static String date_to_yyyy_mm_dd(Date date) {
        String pattern = "yyyy/MM/dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date).toString();
    }

    public static String SHA256Hash(String text) {
        String sha256hex = Hashing.sha256().hashString(text, StandardCharsets.UTF_8).toString();
        return sha256hex;
    }

    public static List<String> JsonGetStringList(JSONObject jsonObject, String key) {
        List<Object> jsonArray = jsonObject.getJSONArray(key).toList();
        List<String> res = new ArrayList<>();
        for (Object obj : jsonArray) {
            res.add(obj.toString());
        }
        return res;
    }
}
