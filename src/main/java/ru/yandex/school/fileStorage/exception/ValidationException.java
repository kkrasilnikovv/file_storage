package ru.yandex.school.fileStorage.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(final String string) {
        super(string);
    }
}
