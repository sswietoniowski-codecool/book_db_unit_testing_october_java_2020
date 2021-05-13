package com.codecool.bookdb.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    @Test
    public void toString_WhenAuthorIsNull_ShouldThrowException() {
       // given
       Author author = null;
       String title = "Some book";

       // when
       Book book = new Book(author, title);

       // then
        assertThrows(NullPointerException.class, () -> book.toString());
    }
}