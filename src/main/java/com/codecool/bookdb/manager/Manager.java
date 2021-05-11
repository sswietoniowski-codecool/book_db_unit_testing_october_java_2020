package com.codecool.bookdb.manager;

import com.codecool.bookdb.view.UserInterface;

public abstract class Manager {
    protected UserInterface ui;

    public Manager(UserInterface ui) {
        this.ui = ui;
    }

    public void run() {
        boolean running = true;

        while (running) {
            ui.printTitle(getName());
            ui.printOption('l', "List");
            ui.printOption('a', "Add");
            ui.printOption('e', "Edit");
            ui.printOption('q', "Quit");

            switch (ui.choice("laeq")) {
                case 'l':
                    list();
                    break;
                case 'a':
                    add();
                    break;
                case 'e':
                    edit();
                    break;
                case 'q':
                    running = false;
                    break;
            }
        }
    }

    public abstract String getName();
    public abstract void list();
    public abstract void add();
    public abstract void edit();
}
