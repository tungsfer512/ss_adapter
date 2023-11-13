package vn.ript.ssadapter.utils;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Utils {

    public static String EDocDir = System.getProperty("user.dir") + "/src/main/resources/edocs";
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/sendAttachs";

    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    public static String time_now() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now).toString();
    }

    public static Path writeFile(Path dir, byte[] bytes) {
        Path filepath = Paths.get(dir.toString());
        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(bytes);
        } catch (Exception e) {
            return null;
        }
        return filepath;
    }
}
