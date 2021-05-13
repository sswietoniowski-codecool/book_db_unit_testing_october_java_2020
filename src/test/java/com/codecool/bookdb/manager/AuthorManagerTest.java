package com.codecool.bookdb.manager;

import com.codecool.bookdb.model.Author;
import com.codecool.bookdb.model.AuthorDao;
import com.codecool.bookdb.view.UserInterface;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class AuthorManagerTest {
    @Test
    public void list_WhenDaoReturnsNObjects_ShouldCallPrintlnNTimes() {
        // given
        UserInterface ui = mock(UserInterface.class);
        AuthorDao authorDao = mock(AuthorDao.class);
        List<Author> authors = Arrays.asList(
                new Author("Jan", "Kowalski", null),
                new Author("Adam", "Nowak", null));
        given(authorDao.getAll()).willReturn(authors);
        AuthorManager authorManager = new AuthorManager(ui, authorDao);

        // when
        authorManager.list();

        // then
        verify(ui, times(2)).println(any(Author.class));
    }

    @Test
    public void edit_WhenWrongAuthorID_ShouldExitPrematurely() {
        // given
        UserInterface ui = mock(UserInterface.class);
        AuthorDao authorDao = mock(AuthorDao.class);
        given(authorDao.get(1)).willReturn(new Author("Jan", "Kowalski", null));
        given(ui.readInt(anyString(), anyInt())).willReturn(0);
        AuthorManager authorManager = new AuthorManager(ui, authorDao);

        // when
        authorManager.edit();

        // then
        verify(ui, times(1)).println("Author not found!");
        verify(authorDao, never()).update(any(Author.class));
    }

    @Test
    public void edit_WhenCorrectAuthorID_ShouldUpdateThroughDao() {
        // given
        UserInterface ui = mock(UserInterface.class);
        AuthorDao authorDao = mock(AuthorDao.class);
        given(authorDao.get(1)).willReturn(new Author("Jan", "Kowalski", null));
        given(ui.readInt(anyString(), anyInt())).willReturn(1);
        AuthorManager authorManager = new AuthorManager(ui, authorDao);

        // when
        authorManager.edit();

        // then
        verify(ui, never()).println("Author not found!");
        verify(authorDao, times(1)).update(any(Author.class));
    }
}