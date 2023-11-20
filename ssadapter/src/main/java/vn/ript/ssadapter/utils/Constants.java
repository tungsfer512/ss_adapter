package vn.ript.ssadapter.utils;

import java.util.Map;

public class Constants {
    public static class TRANG_THAI_VAN_BAN_LIEN_THONG {
        public static String DA_GUI = "01";
        public static String TU_CHOI_TIEP_NHAN = "02";
        public static String DA_TIEP_NHAN = "03";
        public static String DA_PHAN_CONG_XU_LY = "04";
        public static String DANG_XU_LY = "05";
        public static String DA_XU_LY = "06";
        public static String DA_THU_HOI = "13";
        public static String DA_CAP_NHAT = "15";
        public static Map<String, String> MO_TA_TRANG_THAI_VAN_BAN = Map.ofEntries(
                Map.entry(DA_GUI, "Đã gửi đi"),
                Map.entry(TU_CHOI_TIEP_NHAN, "Đã từ chối tiếp nhận"),
                Map.entry(DA_TIEP_NHAN, "Đã tiếp nhận"),
                Map.entry(DA_PHAN_CONG_XU_LY, "ĐÃ phân công xử lý"),
                Map.entry(DANG_XU_LY, "Đang xử lý"),
                Map.entry(DA_XU_LY, "Đã xử lý xong"),
                Map.entry(DA_THU_HOI, "Đã thu hồi"),
                Map.entry(DA_CAP_NHAT, "Đã được cập nhật"));
    }
}
