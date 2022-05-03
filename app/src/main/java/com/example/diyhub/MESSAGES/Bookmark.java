package com.example.diyhub.MESSAGES;

public class Bookmark {
    String id,bookmarked;

    public Bookmark(){}

    public Bookmark(String id, String bookmarked) {
        this.id = id;
        this.bookmarked = bookmarked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(String bookmarked) {
        this.bookmarked = bookmarked;
    }
}
