package be.ucll.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@CssImport("./styles/shared-styles.css")
@Component
public class MainLayout extends AppLayout {
    @Autowired
    public MainLayout(HeaderComponent header, FooterComponent footer) {
        addToNavbar(header);



    }
}
