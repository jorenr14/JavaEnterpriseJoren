package be.ucll.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;


@CssImport("./styles/shared-styles.css")
@Scope(value = "vaadin-ui", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MainLayout extends AppLayout {

    public MainLayout(HeaderComponent header, FooterComponent footer) {
        addToNavbar(header);

    }
}
