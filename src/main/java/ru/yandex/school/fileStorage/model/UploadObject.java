package ru.yandex.school.fileStorage.model;

import lombok.Getter;
import java.util.ArrayList;
@Getter
public class UploadObject {
    private final ArrayList<Item> items= new ArrayList<>();
    private String updateDate;
}
