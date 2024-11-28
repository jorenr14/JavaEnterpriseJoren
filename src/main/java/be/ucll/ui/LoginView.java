package be.ucll.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import be.ucll.services.UserService;



@Route(value = "",layout = MainLayout.class)
@PageTitle("login")
public class LoginView extends VerticalLayout {

	@Autowired
	private UserService userService;

	public LoginView() {

		TextField username = new TextField("Gebruikersnaam");
		PasswordField password = new PasswordField("Wachtwoord");
		Button loginButton = new Button("Inloggen", event -> {

			UI.getCurrent().navigate("search");
		});

		add(username, password, loginButton);
	}
}
