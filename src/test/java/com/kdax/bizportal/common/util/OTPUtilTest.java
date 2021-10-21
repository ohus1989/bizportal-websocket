package com.kdax.bizportal.common.util;

import com.google.zxing.WriterException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;


/*
 * @packageName :
 * @fileName :
 * @author : taekwang.lee
 * @date : 2021-10-21 오후 3:04
 * @description :
 * ===========================================================
 * DATE AUTHOR NOTE
 * -----------------------------------------------------------
 * 2021-10-21 오후 3:04 taekwang.lee 최초 생성
 */
public class OTPUtilTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static String GOOGLE_URL = "https://www.google.com/chart?chs=200x200&chld=M|0&cht=qr&chl=";

    @Test
    void getSecretKey() {
    }

    @Test
    void getTOTPCode() {
    }

    @Test
    void getGoogleOTPAuthURL() {
//        String googleOTPAuthURL = OTPUtil.getGoogleOTPAuthURL(OTPUtil.getSecretKey(), "ddd", "test");
        String googleOTPAuthURL = OTPUtil.getGoogleOTPAuthURL("IYFLHCZWMLZ7PWZGEFFSZTLX3NDL2RGD", "ddd", "test");
        logger.debug(GOOGLE_URL+googleOTPAuthURL);
    }

    @Test
    void getQRImage() {
    }

    @Test
    void getQRtoBufferedImage() {

        String googleOTPAuthURL = OTPUtil.getGoogleOTPAuthURL("IYFLHCZWMLZ7PWZGEFFSZTLX3NDL2RGD", "ddd", "test");

        try {
            BufferedImage qRtoBufferedImage = OTPUtil.getQRtoBufferedImage(googleOTPAuthURL, 200, 200);
            logger.debug("{}",qRtoBufferedImage);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}