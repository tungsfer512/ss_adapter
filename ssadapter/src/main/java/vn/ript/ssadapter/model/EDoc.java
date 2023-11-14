package vn.ript.ssadapter.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "edoc")

public class EDoc {
    
    @Id
    String id;
    String senderDocId;
    String receiverDocId;
    String pid;
    String serviceType;
    String messageType;
    String created_time;
    String updated_time;
    @Column(name = "_data", columnDefinition = "text")
    String data;
    @Column(name = "_from")
    String from;
    @Column(name = "_to")
    String to;
    String sendStatus;
    String receiveStatus;
    String title;
    String notation;
    String sendStatusDesc;
    String receiveStatusDesc;
    

    public EDoc() {
    }

    public EDoc(String id, String senderDocId, String receiverDocId, String pid, String serviceType, String messageType, String created_time, String updated_time, String data, String from, String to, String sendStatus, String receiveStatus, String title, String notation, String sendStatusDesc, String receiveStatusDesc) {
        this.id = id;
        this.senderDocId = senderDocId;
        this.receiverDocId = receiverDocId;
        this.pid = pid;
        this.serviceType = serviceType;
        this.messageType = messageType;
        this.created_time = created_time;
        this.updated_time = updated_time;
        this.data = data;
        this.from = from;
        this.to = to;
        this.sendStatus = sendStatus;
        this.receiveStatus = receiveStatus;
        this.title = title;
        this.notation = notation;
        this.sendStatusDesc = sendStatusDesc;
        this.receiveStatusDesc = receiveStatusDesc;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderDocId() {
        return this.senderDocId;
    }

    public void setSenderDocId(String senderDocId) {
        this.senderDocId = senderDocId;
    }

    public String getReceiverDocId() {
        return this.receiverDocId;
    }

    public void setReceiverDocId(String receiverDocId) {
        this.receiverDocId = receiverDocId;
    }

    public String getPid() {
        return this.pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getCreated_time() {
        return this.created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getUpdated_time() {
        return this.updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSendStatus() {
        return this.sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getReceiveStatus() {
        return this.receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotation() {
        return this.notation;
    }

    public void setNotation(String notation) {
        this.notation = notation;
    }

    public String getSendStatusDesc() {
        return this.sendStatusDesc;
    }

    public void setSendStatusDesc(String sendStatusDesc) {
        this.sendStatusDesc = sendStatusDesc;
    }

    public String getReceiveStatusDesc() {
        return this.receiveStatusDesc;
    }

    public void setReceiveStatusDesc(String receiveStatusDesc) {
        this.receiveStatusDesc = receiveStatusDesc;
    }

    public EDoc id(String id) {
        setId(id);
        return this;
    }

    public EDoc senderDocId(String senderDocId) {
        setSenderDocId(senderDocId);
        return this;
    }

    public EDoc receiverDocId(String receiverDocId) {
        setReceiverDocId(receiverDocId);
        return this;
    }

    public EDoc pid(String pid) {
        setPid(pid);
        return this;
    }

    public EDoc serviceType(String serviceType) {
        setServiceType(serviceType);
        return this;
    }

    public EDoc messageType(String messageType) {
        setMessageType(messageType);
        return this;
    }

    public EDoc created_time(String created_time) {
        setCreated_time(created_time);
        return this;
    }

    public EDoc updated_time(String updated_time) {
        setUpdated_time(updated_time);
        return this;
    }

    public EDoc data(String data) {
        setData(data);
        return this;
    }

    public EDoc from(String from) {
        setFrom(from);
        return this;
    }

    public EDoc to(String to) {
        setTo(to);
        return this;
    }

    public EDoc sendStatus(String sendStatus) {
        setSendStatus(sendStatus);
        return this;
    }

    public EDoc receiveStatus(String receiveStatus) {
        setReceiveStatus(receiveStatus);
        return this;
    }

    public EDoc title(String title) {
        setTitle(title);
        return this;
    }

    public EDoc notation(String notation) {
        setNotation(notation);
        return this;
    }

    public EDoc sendStatusDesc(String sendStatusDesc) {
        setSendStatusDesc(sendStatusDesc);
        return this;
    }

    public EDoc receiveStatusDesc(String receiveStatusDesc) {
        setReceiveStatusDesc(receiveStatusDesc);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof EDoc)) {
            return false;
        }
        EDoc eDoc = (EDoc) o;
        return Objects.equals(id, eDoc.id) && Objects.equals(senderDocId, eDoc.senderDocId) && Objects.equals(receiverDocId, eDoc.receiverDocId) && Objects.equals(pid, eDoc.pid) && Objects.equals(serviceType, eDoc.serviceType) && Objects.equals(messageType, eDoc.messageType) && Objects.equals(created_time, eDoc.created_time) && Objects.equals(updated_time, eDoc.updated_time) && Objects.equals(data, eDoc.data) && Objects.equals(from, eDoc.from) && Objects.equals(to, eDoc.to) && Objects.equals(sendStatus, eDoc.sendStatus) && Objects.equals(receiveStatus, eDoc.receiveStatus) && Objects.equals(title, eDoc.title) && Objects.equals(notation, eDoc.notation) && Objects.equals(sendStatusDesc, eDoc.sendStatusDesc) && Objects.equals(receiveStatusDesc, eDoc.receiveStatusDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderDocId, receiverDocId, pid, serviceType, messageType, created_time, updated_time, data, from, to, sendStatus, receiveStatus, title, notation, sendStatusDesc, receiveStatusDesc);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", senderDocId='" + getSenderDocId() + "'" +
            ", receiverDocId='" + getReceiverDocId() + "'" +
            ", pid='" + getPid() + "'" +
            ", serviceType='" + getServiceType() + "'" +
            ", messageType='" + getMessageType() + "'" +
            ", created_time='" + getCreated_time() + "'" +
            ", updated_time='" + getUpdated_time() + "'" +
            ", data='" + getData() + "'" +
            ", from='" + getFrom() + "'" +
            ", to='" + getTo() + "'" +
            ", sendStatus='" + getSendStatus() + "'" +
            ", receiveStatus='" + getReceiveStatus() + "'" +
            ", title='" + getTitle() + "'" +
            ", notation='" + getNotation() + "'" +
            ", sendStatusDesc='" + getSendStatusDesc() + "'" +
            ", receiveStatusDesc='" + getReceiveStatusDesc() + "'" +
            "}";
    }

}
