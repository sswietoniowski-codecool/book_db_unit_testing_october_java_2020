package com.codecool.bookdb.manager;

import com.codecool.bookdb.model.Author;
import com.codecool.bookdb.model.AuthorDao;
import com.codecool.bookdb.model.Book;
import com.codecool.bookdb.model.BookDao;
import com.codecool.bookdb.view.UserInterface;

public class BookManager extends Manager {
    private BookDao bookDao;
    private AuthorDao authorDao;

    public BookManager(UserInterface ui, BookDao bookDao, AuthorDao authorDao) {
        super(ui);
        this.bookDao = bookDao;
        this.authorDao = authorDao;
    }

    @Override
    protected String getName() {
        return "Book Manager";
    }

    @Override
    protected void list() {
        for (Book book : bookDao.getAll()) {
            ui.println(book);
        }
    }

    @Override
    protected void add() {
        ui.println("Authors:");
        for (Author author : authorDao.getAll()) {
            ui.println(author);
        }

        int authorId = ui.readInt("Author ID", 0);
        String title = ui.readString("Title", "X");

        Author author = authorDao.get(authorId);

        if (author == null) {
            ui.println("Author not found!");
            return;
        }

        bookDao.add(new Book(author, title));
    }

    @Override
    protected void edit() {
        int bookId = ui.readInt("Book ID", 0);

        Book book = bookDao.get(bookId);

        if (book == null) {
            ui.println("Book not found!");
            return;
        }

        ui.println("Authors:");
        for (Author author : authorDao.getAll()) {
            ui.println(author);
        }

        int authorId = ui.readInt("Author ID", book.getAuthor().getId());
        String title = ui.readString("Title", book.getTitle());
        Author author = authorDao.get(authorId);

        if (author == null) {
            ui.println("Author not found!");
            return;
        }

        book.setAuthor(author);
        book.setTitle(title);

        bookDao.update(book);
    }
}
