package com.example.museumar.model;

public class Exhibit {
    private String id;
    private boolean isARCapable;
    private String name;
    private String description;
    private String imageUrl;

    // Конструктор с параметрами
    public Exhibit(String id, boolean isARCapable, String name, String description, String imageUrl) {
        this.id = id;
        this.isARCapable = isARCapable;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Конструктор без параметров
    public Exhibit() {
    }

    // Геттеры
    public String getId() {
        return id;
    }

    public boolean isARCapable() {
        return isARCapable;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Метод toString для удобства отображения информации об объекте
    @Override
    public String toString() {
        return "Exhibit{" +
                "id='" + id + '\'' +
                ", isARCapable=" + isARCapable +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
