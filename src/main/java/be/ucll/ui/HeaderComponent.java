package be.ucll.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "vaadin-ui", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HeaderComponent extends Div {
    public HeaderComponent() {
        setId("header");
        addClassName("header");

        setWidthFull();
        addClassName("header");

        Span shopInfo = new Span("Welkom bij Enterprise Store!");
        shopInfo.getStyle().set("margin-left", "10px");

        Button logoutButton = new Button("Logout", event -> {
            UI.getCurrent().navigate("login");
            VaadinSession.getCurrent().close();
        });
        logoutButton.setId("logout-button");


        HorizontalLayout layout = new HorizontalLayout(shopInfo, logoutButton);
        layout.setWidthFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        add(layout);

    }
}
