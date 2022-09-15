package ru.yandex.school.fileStorage.model;

import java.util.ArrayList;
public class File extends Item {
    public File(String type, String url, String id, String parentId, Integer size, String dateOfUpdate,
                ArrayList<String> children) {
        super(type, url, id, parentId, size, dateOfUpdate, children);
    }
}
