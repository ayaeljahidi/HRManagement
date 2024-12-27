package View;
import javax.swing.*;
import java.awt.*;
public class LoginView extends JFrame {
	 public JButton login;
	 private JPanel p1,p2,p;
	 public JTextField username;
	 public  JTextField password;
	 public LoginView(){
		 setTitle("Gestion des Employés et Congés");
	     setSize(400, 150);	     
	     p1=new JPanel(new GridLayout(2,2,5,5));
	     p1.add(new JLabel("Username:"));
	     username= new JTextField();
	     
	     p1.add(username);
	     p1.add(new JLabel("Password:"));
	     password= new JPasswordField();
		 p1.add(password);
		 
		 p2=new JPanel(new FlowLayout());
		 login = new JButton("Login");
		 login.setBackground(Color.GREEN);
		 p2.add(login);
		 
		 p= new JPanel(new BorderLayout());
		 p.add(p1,BorderLayout.NORTH);
		 p.add(p2,BorderLayout.SOUTH);
		 
		 this.add(p);
		 this.setBounds(500,350,400,150);
		 this.setVisible(true);
		 this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	
		 
	 }

}
