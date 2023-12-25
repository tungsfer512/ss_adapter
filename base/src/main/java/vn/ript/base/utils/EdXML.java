package vn.ript.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.util.ResourceUtils;

import com.vnpt.xml.ed.Ed;
import com.vnpt.xml.ed.parser.EdXmlParser;
import com.vnpt.xml.status.Status;
import com.vnpt.xml.status.parser.StatusXmlParser;

public class EdXML {
    public static Ed readDocument(String fileName) {
        Ed ed = null;
        try {
            // Thread.sleep(3000);
            File file = ResourceUtils.getFile(Utils.EDOC_DIR + fileName);
            InputStream inputStream = new FileInputStream(file);
            ed = EdXmlParser.getInstance().parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ed;
    }
    
    public static Ed readDocument(File file) {
        Ed ed = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            ed = EdXmlParser.getInstance().parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ed;
    }
    
    public static Ed readDocument(InputStream inputStream) {
        Ed ed = null;
        try {
            ed = EdXmlParser.getInstance().parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ed;
    }

    public static Status readStatus(String fileName) {
        Status status = null;
        try {
            File file = ResourceUtils.getFile(Utils.EDOC_DIR + fileName);
            InputStream inputStream = new FileInputStream(file);
            status = StatusXmlParser.parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public static Status readStatus(File file) {
        Status status = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            status = StatusXmlParser.parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public static Status readStatus(InputStream inputStream) {
        Status status = null;
        try {
            status = StatusXmlParser.parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
}