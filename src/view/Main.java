package view;

import javafx.application.Application;

public class Main {
    public static void main(String[] args){
//        Model model = new Model();
//        view view = new view();
//        model.addObserver(view);
//        model.change();

        Application.launch(View.class, args);
    }
}