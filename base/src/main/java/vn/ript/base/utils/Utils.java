package vn.ript.base.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
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
import org.springframework.web.multipart.MultipartFile;

import com.google.common.hash.Hashing;

public class Utils {

    public static String EDOC_DIR = System.getenv("API_WORK_DIR") + "/src/main/resources/edocs/";
    public static String ATTACHMENT_DIR = System.getenv("API_WORK_DIR") + "/src/main/resources/attachments/";
    public static String SS_IP = System.getenv("SS_IP") != null ? System.getenv("SS_IP") : "localhost";
    public static String SS_QLVB_SERVICE_CODE = System.getenv("SS_QLVB_SERVICE_CODE") != null
            ? System.getenv("SS_QLVB_SERVICE_CODE")
            : "";
    public static String SS_BASE_URL = "https://" + SS_IP;

    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    public static String ENCODE_TO_BASE64(String file_path) {
        try {
            File file = ResourceUtils.getFile(file_path);
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new IllegalStateException("could not read file in " + file_path, e);
        }
    }

    public static String ENCODE_TO_BASE64(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new IllegalStateException("could not read file in " + file.toPath(), e);
        }
    }

    public static String ENCODE_TO_BASE64(byte[] fileContent) {
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static String DATETIME_NOW() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now).toString();
    }

    public static String DATE_NOW() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now).toString();
    }

    public static String DATE_TO_YYYY_MM_DD(Date date) {
        String pattern = "yyyy/MM/dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date).toString();
    }

    public static Date YYYY_MM_DD_TO_DATE(String date_text) {
        try {
            String pattern = "yyyy/MM/dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.parse(date_text);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String DATE_TO_YYYY_MM_DD_HH_MM_SS(Date date) {
        String pattern = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date).toString();
    }

    public static String SHA256_HASH(String text) {
        String sha256hex = Hashing.sha256().hashString(text, StandardCharsets.UTF_8).toString();
        return sha256hex;
    }

    public static List<String> JSON_GET_STRING_LIST(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            List<Object> jsonArray = jsonObject.getJSONArray(key).toList();
            List<String> res = new ArrayList<>();
            for (Object obj : jsonArray) {
                res.add(obj.toString());
            }
            return res;
        } else {
            return new ArrayList<>();
        }
    }

    public static String EXEC_SHELL_COMMAND(List<String> command) {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectOutput(ProcessBuilder.Redirect.PIPE);
        builder.redirectError(ProcessBuilder.Redirect.PIPE);
        Process process;
        JSONObject jsonObject = new JSONObject();
        try {
            process = builder.start();
            InputStream stdout = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            StringBuilder output = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            reader.close();
            InputStream stderr = process.getErrorStream();
            reader = new BufferedReader(new InputStreamReader(stderr));
            StringBuilder error = new StringBuilder();
            line = null;
            while ((line = reader.readLine()) != null) {
                error.append(line);
            }
            reader.close();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                jsonObject.put("status", "success");
                jsonObject.put("data", output.toString());
            } else {
                jsonObject.put("status", "failed");
                jsonObject.put("data", error.toString());
            }
            return jsonObject.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            jsonObject.put("status", "failed");
            jsonObject.put("data", "loi loi");
            return jsonObject.toString();
        }
    }

    public static File MULTIPART_FILE_TO_FILE(MultipartFile multipartFile, String type) {

        String file_dir = ATTACHMENT_DIR;
        if (type.equalsIgnoreCase(Constants.LOAI_FILE.EDOC.ma())) {
            file_dir = EDOC_DIR;
        } else if (type.equalsIgnoreCase(Constants.LOAI_FILE.ATTACHMENT.ma())) {
            file_dir = ATTACHMENT_DIR;
        }
        String[] originalFileNames = multipartFile.getOriginalFilename().split("\\.");
        String fileExt = originalFileNames[originalFileNames.length - 1];
        String fileName = originalFileNames[0] + "_" + UUID() + "." + fileExt;
        Path filePath = Paths.get(file_dir, fileName);

        try {
            Files.write(filePath, multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath.toFile();
    }
}
