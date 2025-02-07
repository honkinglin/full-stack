package com.fullstack.mall.common;

import com.fullstack.mall.exception.MallException;
import com.fullstack.mall.exception.MallExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Constant {
    public static final String SALT = "8d78869f470951332959580424d4bf4f";

    public static final String MALL_USER = "mall_user";

    public static final String EMAIL_SUBJECT = "您的验证码";
    public static final String EMAIL_FROM = "hq.lin@foxmail.com";
    public static final String WATER_MARK_JPG = "watermark.jpg";
    public static final Integer IMAGE_SIZE = 400;
    public static final Float IMAGE_OPACITY = 0.5f;

    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    public interface ProductListOrderBy {
        Set<String> PRICE_ORDER_ENUM = Set.of("price desc", "price asc");
    }

    public interface SaleStatus {
        int NOT_SALE = 0; // Not for sale
        int SALE = 1; // For sale
    }

    public interface Cart {
        int UN_CHECKED = 0; // Unchecked
        int CHECKED = 1; // Checked
    }

    public enum OrderStatusEnum {
        CANCELED(0, "Canceled"),
        NO_PAY(10, "Unpaid"),
        PAID(20, "Paid"),
        DELIVERED(30, "Delivered"),
        SHIPPED(40, "Shipped"),
        TRADE_SUCCESS(50, "Trade success"),
        TRADE_CLOSE(60, "Trade closed");

        private String value;
        private int code;

        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new MallException(MallExceptionEnum.NO_ENUM);
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public static final String JWT_KEY = "mall";
    public static final String JWT_TOKEN = "jwt_token";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_ROLE = "user_role";
    public static final Long EXPIRE_TIME = 60 * 1000 * 60 * 24 * 1000L;//单位是毫秒
}
