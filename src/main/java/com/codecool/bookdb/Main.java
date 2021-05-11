package com.codecool.bookdb;

import com.codecool.bookdb.manager.BookDbManager;
import com.codecool.bookdb.view.UserInterface;

public class Main {
    public static void main(String[] args) {
        try
        {
            UserInterface ui = new UserInterface(System.in, System.out);
            new BookDbManager(ui).run();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
