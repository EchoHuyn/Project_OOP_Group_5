package extensions;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {

    public static void sendEmail(String recipient, String subject, String content) {
        // Cấu hình server Gmail SMTP
        String host = "smtp.gmail.com";
        final String senderEmail = "npqt4s0u1@gmail.com";  // Thay bằng email của bạn
        final String senderPassword = "lzas dghv vwlf cpay";  // Thay bằng mật khẩu email của bạn

        // Thiết lập các thuộc tính cho kết nối SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Tạo phiên với thông tin xác thực
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Tạo email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(content);

            // Gửi email
            Transport.send(message);
            System.out.println("Email đã được gửi thành công!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    public static void sendEmail_OTP(String OTP, String recipient) {
        // Gọi hàm sendEmail để gửi email xác nhận
        String subject = "Xác nhận đăng ký";
        String content = "Cảm ơn bạn đã đăng ký! Đây là email xác nhận. Mã OTP của bạn là: " + OTP;
        sendEmail(recipient, subject, content);
    }
}