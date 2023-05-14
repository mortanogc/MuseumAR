package com.example.museumar.model;

public class News {
    private String title;
    private String content;
    private String imageUrl;
    private String date;

    // Конструктор с параметрами
    public News(String title, String content, String imageUrl, String date) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
    }

    // Конструктор без параметров
    public News() {
    }

    // Геттеры
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDate() {
        return date;
    }
    // Метод toString для удобства отображения информации об объекте
    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
