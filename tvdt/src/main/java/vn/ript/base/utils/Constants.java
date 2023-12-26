package vn.ript.base.utils;

import java.util.HashMap;

public class Constants {

    private static HashMap<String, TRANG_THAI_VAN_BAN> TRANG_THAI_VAN_BAN_MAP = new HashMap<>();
    private static HashMap<String, TRANG_THAI_GOI_TIN> TRANG_THAI_GOI_TIN_MAP = new HashMap<>();
    private static HashMap<String, TRANG_THAI_LIEN_THONG> TRANG_THAI_LIEN_THONG_MAP = new HashMap<>();
    private static HashMap<String, LOAI_FILE> LOAI_FILE_MAP = new HashMap<>();
    private static HashMap<String, TRANG_THAI_YEU_CAU_THU_VIEN> TRANG_THAI_YEU_CAU_THU_VIEN_MAP = new HashMap<>();
    private static HashMap<String, TRANG_THAI_BAO_CAO_THU_VIEN> TRANG_THAI_BAO_CAO_THU_VIEN_MAP = new HashMap<>();

    public enum TRANG_THAI_VAN_BAN {
        CHO_TIEP_NHAN("01",
                "Văn bản đang chờ tiếp nhận",
                "Văn bản đang chờ tiếp nhận"),
        DA_TU_CHOI_TIEP_NHAN("02",
                "Văn bản đã bị từ chối tiếp nhận",
                "Đã từ chối tiếp nhận văn bản"),
        DA_TIEP_NHAN_CHO_PHAN_CONG("03",
                "Văn bản đã được tiếp nhận, đang chờ phân công xử lý",
                "Đã tiếp nhận văn bản, yêu cầu phân công xử lý"),
        DA_PHAN_CONG_CHO_XU_LY("04",
                "Văn bản đã được phân công, đang chờ xử lý",
                "Đã phân công xử lý văn bản, yêu cầu xử lý"),
        DANG_XU_LY("05",
                "Văn bản đang được xử lý",
                "Đang xử lý văn bản"),
        DA_HOAN_THANH_XU_LY("06",
                "Văn bản đã được xử lý",
                "Văn bản đã xử lý xong"),
        DA_YEU_CAU_THU_HOI("103",
                "Đã thông báo thu hồi văn bản",
                "Văn bản có thông báo thu hồi"),
        DA_THU_HOI("13",
                "Đã thu hồi văn bản",
                "Văn bản đã bị thu hồi"),
        DA_YEU_CAU_CAP_NHAT("104",
                "Đã yêu cầu cập nhật văn bản",
                "Văn bản có yêu cầu cập nhật"),
        DA_DONG_Y_CAP_NHAT("15",
                "Văn bản đã có phiên bản cập nhật",
                "Văn bản đã có phiên bản cập nhật"),
        DA_TU_CHOI_CAP_NHAT("16",
                "Văn bản đã bị từ chối cập nhật",
                "Đã từ chối phiên bản cập nhật của văn bản, đây là phiên bản cũ");

        private final String maTrangThai;
        private final String moTaTrangThaiGui;
        private final String moTaTrangThaiNhan;

        TRANG_THAI_VAN_BAN(final String maTrangThai, final String moTaTrangThaiGui, final String moTaTrangThaiNhan) {
            this.maTrangThai = maTrangThai;
            this.moTaTrangThaiGui = moTaTrangThaiGui;
            this.moTaTrangThaiNhan = moTaTrangThaiNhan;
            TRANG_THAI_VAN_BAN_MAP.put(maTrangThai, this);
        }

        public String maTrangThai() {
            return this.maTrangThai;
        }

        public String moTaTrangThaiGui() {
            return this.moTaTrangThaiGui;
        }

        public String moTaTrangThaiNhan() {
            return this.moTaTrangThaiNhan;
        }

        public static TRANG_THAI_VAN_BAN getByMaTrangThai(String maTrangThai) {
            return TRANG_THAI_VAN_BAN_MAP.get(maTrangThai);
        }
    }

    public enum TRANG_THAI_GOI_TIN {
        DA_GUI_KHONG_PHAN_HOI("00",
                "Gói tin trạng thái đã chuyển đi, không yêu cầu phản hồi",
                "Gói tin trạng thái đã nhận, không yêu cầu phản hồi"),
        CHO_PHAN_HOI("01",
                "Gói tin trạng thái đang chờ phản hồi",
                "Gói tin trạng thái đang chờ phản hồi"),
        DA_HOAN_THANH_XU_LY("06",
                "Gói tin trạng thái đã phản hồi",
                "Gói tin trạng thái đã phản hồi");

        private final String maTrangThai;
        private final String moTaTrangThaiGui;
        private final String moTaTrangThaiNhan;

        private TRANG_THAI_GOI_TIN(final String maTrangThai, final String moTaTrangThaiGui,
                final String moTaTrangThaiNhan) {
            this.maTrangThai = maTrangThai;
            this.moTaTrangThaiGui = moTaTrangThaiGui;
            this.moTaTrangThaiNhan = moTaTrangThaiNhan;
            TRANG_THAI_GOI_TIN_MAP.put(maTrangThai, this);
        }

        public String maTrangThai() {
            return this.maTrangThai;
        }

        public String moTaTrangThaiGui() {
            return this.moTaTrangThaiGui;
        }

        public String moTaTrangThaiNhan() {
            return this.moTaTrangThaiNhan;
        }

        public static TRANG_THAI_GOI_TIN getByMaTrangThai(String maTrangThai) {
            return TRANG_THAI_GOI_TIN_MAP.get(maTrangThai);
        }
    }

    public enum TRANG_THAI_LIEN_THONG {
        THANH_CONG("done", "Đã gửi đi thành công"),
        THAT_BAI("fail", "Gửi đến đơn vị nhận lỗi");

        private final String maTrangThai;
        private final String moTaTrangThai;

        private TRANG_THAI_LIEN_THONG(final String maTrangThai, final String moTaTrangThai) {
            this.maTrangThai = maTrangThai;
            this.moTaTrangThai = moTaTrangThai;
            TRANG_THAI_LIEN_THONG_MAP.put(maTrangThai, this);
        }

        public String maTrangThai() {
            return this.maTrangThai;
        }

        public String moTaTrangThai() {
            return this.moTaTrangThai;
        }

        public static TRANG_THAI_GOI_TIN getByMaTrangThai(String maTrangThai) {
            return TRANG_THAI_GOI_TIN_MAP.get(maTrangThai);
        }
    }

    public enum LOAI_FILE {
        EDOC("EDOC", "File văn bản điện tử/gói tin trạng thái"),
        ATTACHMENT("ATTACHMENT", "File đính kèm");

        private final String ma;
        private final String moTa;

        private LOAI_FILE(final String ma, final String moTa) {
            this.ma = ma;
            this.moTa = moTa;
            LOAI_FILE_MAP.put(ma, this);
        }

        public String ma() {
            return this.ma;
        }

        public String moTa() {
            return this.moTa;
        }

        public LOAI_FILE getByMa(String ma) {
            return LOAI_FILE_MAP.get(ma);
        }

    }

    public enum TRANG_THAI_YEU_CAU_THU_VIEN {
        PENDING("PENDING", "Yêu cầu đang chờ phản hồi"),
        APPROVED("APPROVED", "Yêu cầu đã được chấp thuận"),
        DECLINED("DECLINED", "Yêu cầu đã bị từ chối");

        private final String ma;
        private final String moTa;

        private TRANG_THAI_YEU_CAU_THU_VIEN(final String ma, final String moTa) {
            this.ma = ma;
            this.moTa = moTa;
            TRANG_THAI_YEU_CAU_THU_VIEN_MAP.put(ma, this);
        }

        public String ma() {
            return this.ma;
        }

        public String moTa() {
            return this.moTa;
        }

        public TRANG_THAI_YEU_CAU_THU_VIEN getByMa(String ma) {
            return TRANG_THAI_YEU_CAU_THU_VIEN_MAP.get(ma);
        }

    }

    public enum TRANG_THAI_BAO_CAO_THU_VIEN {
        PENDING("PENDING", "Báo cáo chưa đọc"),
        APPROVED("APPROVED", "Báo cáo đã đọc");

        private final String ma;
        private final String moTa;

        private TRANG_THAI_BAO_CAO_THU_VIEN(final String ma, final String moTa) {
            this.ma = ma;
            this.moTa = moTa;
            TRANG_THAI_BAO_CAO_THU_VIEN_MAP.put(ma, this);
        }

        public String ma() {
            return this.ma;
        }

        public String moTa() {
            return this.moTa;
        }

        public TRANG_THAI_BAO_CAO_THU_VIEN getByMa(String ma) {
            return TRANG_THAI_BAO_CAO_THU_VIEN_MAP.get(ma);
        }

    }
}
