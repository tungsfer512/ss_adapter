package vn.ript.ssadapter.utils;

public class CustomResponseData<T> {

    public interface RESPONSE_ERROR_DESC {
        String THANH_CONG = "Thanh cong";
        String THAT_BAI = "InvalidArgument";
    }

    public interface RESPONSE_ERROR_CODE {
        String THANH_CONG = "0";
        String THAT_BAI = "-1";
    }

    public interface RESPONSE_STATUS {
        String THANH_CONG = "OK";
        String THAT_BAI = "FAIL";
    }

    T data; 
    String ErrorDesc;
    String ErrorCode;
    String status;

    public CustomResponseData() {
        this.ErrorCode = RESPONSE_ERROR_CODE.THANH_CONG;
        this.ErrorDesc = RESPONSE_ERROR_DESC.THANH_CONG;
        this.status = RESPONSE_STATUS.THANH_CONG;
        this.data = null;
    }

    public CustomResponseData(T data) {
        this.ErrorCode = RESPONSE_ERROR_CODE.THANH_CONG;
        this.ErrorDesc = RESPONSE_ERROR_DESC.THANH_CONG;
        this.status = RESPONSE_STATUS.THANH_CONG;
        this.data = data;
    }

    public CustomResponseData<T> error() {
        this.ErrorCode = RESPONSE_ERROR_CODE.THAT_BAI;
        this.ErrorDesc = RESPONSE_ERROR_DESC.THAT_BAI;
        this.status = RESPONSE_STATUS.THAT_BAI;
        this.data = null;
        return this;
    }

    public CustomResponseData<T> error(T data) {
        this.ErrorCode = RESPONSE_ERROR_CODE.THAT_BAI;
        this.ErrorDesc = RESPONSE_ERROR_DESC.THAT_BAI;
        this.status = RESPONSE_STATUS.THAT_BAI;
        this.data = data;
        return this;
    }


}
