package ru.otus.demen.service;

public interface LocalizedMessageService {
    String getMessage(String key);
    String getMessage(String key, Object... params);
}
