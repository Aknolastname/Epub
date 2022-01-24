package com.community.epub;

import nl.siegmann.epublib.domain.Book;

public class BookItem {
    String path;
    Book book;

    public BookItem() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
