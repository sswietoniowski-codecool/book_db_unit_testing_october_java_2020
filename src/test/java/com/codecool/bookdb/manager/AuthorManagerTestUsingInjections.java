package com.codecool.bookdb.manager;

import com.codecool.bookdb.model.Author;
import com.codecool.bookdb.model.AuthorDao;
import com.codecool.bookdb.view.UserInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthorManagerTestUsingInjections {
    // helper materials: https://www.baeldung.com/mockito-junit-5-extension
    @Mock
    public UserInterface ui;
    @Mock
    public AuthorDao authorDao;
    @InjectMocks
    public AuthorManager authorManager;

    @Test
    public void list_WhenDaoReturnsNObjects_ShouldCallPrintlnNTimes() {
        // given
        List<Author> authors = Arrays.asList(
                new Author("Jan", "Kowalski", null),
                new Author("Adam", "Nowak", null));
        given(authorDao.getAll()).willReturn(authors);

        // when
        authorManager.list();

        // then
        verify(ui, times(2)).println(any(Author.class));
    }

    @Test
    public void edit_WhenWrongAuthorID_ShouldExitPrematurely() {
        // given
        lenient().when(authorDao.get(1)).thenReturn(new Author("Jan", "Kowalski", null));
        given(ui.readInt(anyString(), anyInt())).willReturn(0);

        // when
        authorManager.edit();

        // then
        verify(ui, times(1)).println("Author not found!");
        verify(authorDao, never()).update(any(Author.class));
    }

    @Test
    public void edit_WhenCorrectAuthorID_ShouldUpdateThroughDao() {
        // given
        lenient().when(authorDao.get(1)).thenReturn(new Author("Jan", "Kowalski", null));
        given(ui.readInt(anyString(), anyInt())).willReturn(1);

        // when
        authorManager.edit();

        // then
        verify(ui, never()).println("Author not found!");
        verify(authorDao, times(1)).update(any(Author.class));
    }
}
