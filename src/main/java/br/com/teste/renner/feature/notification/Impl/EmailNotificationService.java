package br.com.teste.renner.feature.notification.Impl;

public interface EmailNotificationService {
    public void sendEmail(String to, String subject, String body);
}

