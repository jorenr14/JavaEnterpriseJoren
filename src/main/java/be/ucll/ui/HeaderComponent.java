package be.ucll.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Component;

@Component
public class HeaderComponent extends Div {
    public HeaderComponent() {
        setId("header");
        addClassName("header");


        Span shopInfo = new Span("Welkom bij Enterprise Store!");

        Button logoutButton = new Button("Logout", event -> {
            UI.getCurrent().navigate("login");
            // Invalidate session (optioneel)
            VaadinSession.getCurrent().close();
        });

        logoutButton.getStyle().set("margin-left", "auto");

        HorizontalLayout layout = new HorizontalLayout(shopInfo, logoutButton);
        layout.setWidthFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        add(layout);
    }
}
