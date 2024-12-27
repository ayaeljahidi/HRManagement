package Main;

import Controller.LoginController;
import View.LoginView;

public class MainLogin {
    public static void main(String[] args) {
        LoginView loginView = new LoginView();
        new LoginController(loginView);
    }
}
