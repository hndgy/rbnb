package fr.orleans.univ.miage.m2.rbnbmailingservice.consumers;

public class EmailMessage {
    private String toEmail;
    private String subject;
    private String content;

    public String getToEmail() {
        return toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }
}
