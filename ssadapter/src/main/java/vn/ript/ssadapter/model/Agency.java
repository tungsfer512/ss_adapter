package vn.ript.ssadapter.model;

import javax.persistence.*;

@Entity
@Table(name = "agencies")

public class Agency {
    
    @Id
    String id;
    String pid;
    String code;
    String pcode;
    String name;
    String mail;
    String mobile;
    String fax;


    public Agency() {
    }

    public Agency(String id, String pid, String code, String pcode, String name, String mail, String mobile, String fax) {
        this.id = id;
        this.pid = pid;
        this.code = code;
        this.pcode = pcode;
        this.name = name;
        this.mail = mail;
        this.mobile = mobile;
        this.fax = fax;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return this.pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPcode() {
        return this.pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Agency id(String id) {
        setId(id);
        return this;
    }

    public Agency pid(String pid) {
        setPid(pid);
        return this;
    }

    public Agency code(String code) {
        setCode(code);
        return this;
    }

    public Agency pcode(String pcode) {
        setPcode(pcode);
        return this;
    }

    public Agency name(String name) {
        setName(name);
        return this;
    }

    public Agency mail(String mail) {
        setMail(mail);
        return this;
    }

    public Agency mobile(String mobile) {
        setMobile(mobile);
        return this;
    }

    public Agency fax(String fax) {
        setFax(fax);
        return this;
    }
}
