package com.codecool.bookdb.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

class AuthorTest {
    @Test
    public void constructor_WhenCalled_ShouldInitializeFields_JUnit() {
        // given
        String firstName = "Jan";
        String lastName = "Kowalski";

        // when
        Author author = new Author(firstName, lastName, null);

        // then
        assertEquals(firstName, author.getFirstName(), "First name not initialized properly");
        assertEquals(lastName, author.getLastName(), "Last name not initialized properly");
    }

    @Test
    public void constructor_WhenCalled_ShouldInitializeFields_Hamcrest() {
        // given
        String firstName = "Jan";
        String lastName = "Kowalski";

        // when
        Author author = new Author(firstName, lastName, null);

        // then
        assertThat(author.getFirstName(), is(equalTo(firstName)));
        assertThat(author.getLastName(), is(equalTo(lastName)));
    }
}