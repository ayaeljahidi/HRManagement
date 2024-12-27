package Controller;
import DAO.LoginDAO;
import DAO.LoginDAOImpl;
import Main.MainEMployee;
import Model.Login;
import View.LoginView;
import javax.swing.*;

public class LoginController {
    private LoginView view;
    private LoginDAO loginDAO;
    public LoginController(LoginView view) {
        this.view = view;
        this.loginDAO = new LoginDAOImpl();

        this.view.login.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = view.username.getText();
        String password = new String(view.password.getText());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Login login = new Login(username, password);

        if (loginDAO.validate(login)) {            
            // Fermer la fenêtre de login
            view.dispose();
            // Lancer la méthode start() de MainEMployee au lieu de main()
            MainEMployee.start(); 

        } else {
            JOptionPane.showMessageDialog(view, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
  
}
