package ru.yandex.school.fileStorage.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(final String string) {
        super(string);
    }
}
