package win.yayuanzi8.userservice.service;


public interface MailService {
    void sendSimpleEmail(String to, String subject, String content);
    void sendHtmlEmail(String to, String subject, String content);
    void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId);
}
