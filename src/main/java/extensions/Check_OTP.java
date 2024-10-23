package extensions;

import java.util.Random;

public class Check_OTP {
    // Hàm sinh OTP ngẫu nhiên
    public static String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
