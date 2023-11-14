package vn.ript.ssadapter.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.ResourceUtils;

public class Utils {

    public static String EDocDir = System.getenv("API_WORK_DIR") + "/src/main/resources/edocs/";
    public static String uploadDir = System.getenv("API_WORK_DIR") + "/src/main/resources/vanban/";
    public static String ipSS = System.getenv("IP_SS") != null ? System.getenv("IP_SS") : "localhost";
    public static Map<String, String> envs = System.getenv();

    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    public static String encodeEdXmlFileToBase64(String file_path) {
        try {
            File file = ResourceUtils.getFile(file_path);
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new IllegalStateException("could not read file in " + file_path, e);
        }
    }

    public static String datetime_now() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now).toString();
    }

}
