package ru.yandex.school.fileStorage.storage;

import org.springframework.stereotype.Component;
import ru.yandex.school.fileStorage.model.Item;


import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;

@Component
public class ItemStorage {

    private final HashMap<String, Item> items = new HashMap<>();

    public void addItems(Item item) {
        items.put(item.getId(), item);
        calculateConnections();
    }

    public void updateItem(Item item) {
        items.replace(item.getId(), item);
        calculateConnections();
    }

    public void removeItem(String id) {
        if (items.get(id).getParentId() != null && items.containsKey(items.get(id).getParentId())) {
            items.get(items.get(id).getParentId()).getChildren().remove(id);
            items.get(items.get(id).getParentId()).removeSize(items.get(id).getSize());
        }
        if (items.get(id).getChildren() != null && !items.get(id).getChildren().isEmpty()) {
            for (String childrenId : items.get(id).getChildren()) {
                if (items.get(childrenId).getType().equals("FOLDER")) {
                    removeItem(childrenId);
                } else {
                    items.remove(childrenId);
                }
            }
        }
        items.remove(id);
    }

    public Item getItemById(String id) {
        return items.get(id);
    }

    private void calculateConnections() {//добавялем или изменяем детей\родитилей,расчитываем размер и дату
        for (Item item : items.values()) {
            if (item.getParentId() != null && items.containsKey(item.getParentId()) &&
                    !items.get(items.get(item.getId()).getParentId()).getChildren().contains(item.getId())) {
                items.get(item.getParentId()).addChildren(item.getId());
                if (parseDate(items.get(item.getParentId()).getDateOfUpdate()).
                        isBefore(parseDate(item.getDateOfUpdate()))) {//работа с датой
                    items.get(item.getParentId()).setDateOfUpdate(item.getDateOfUpdate());
                }
            }
            if (item.getChildren() != null && !item.getChildren().isEmpty()) {
                for (String idChildren : item.getChildren()) {
                    if (items.get(idChildren).getParentId() == null ||
                            !items.get(idChildren).getParentId().equals(item.getId())) {
                        items.get(item.getId()).removeChildren(idChildren);
                    }
                    if (parseDate(item.getDateOfUpdate()).isBefore(parseDate(items.get(idChildren).getDateOfUpdate()))) {
                        item.setDateOfUpdate(items.get(idChildren).getDateOfUpdate());//работа с датой
                    }
                }
            }
        }
    }

    private ZonedDateTime parseDate(String date) {
        Instant instant = Instant.parse(date);
        return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
