package vn.ript.ssadapter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.util.ResourceUtils;

import com.vnpt.xml.ed.Ed;
import com.vnpt.xml.ed.parser.EdXmlParser;
import com.vnpt.xml.status.Status;
import com.vnpt.xml.status.parser.StatusXmlParser;

public class EdXML {
    public static Ed readEdoc(String fileName) {
        Ed ed = null;
        try {
            Thread.sleep(3000);
            File file = ResourceUtils.getFile(Utils.EDocDir + fileName);
            InputStream inputStream = new FileInputStream(file);
            ed = EdXmlParser.getInstance().parse(inputStream);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return ed;
    }

    public static Status readStatus(String fileName) {
        Status status = null;
        try {
            File file = ResourceUtils.getFile(Utils.EDocDir + fileName);
            InputStream inputStream = new FileInputStream(file);
            status = StatusXmlParser.parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
}
