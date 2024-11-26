package be.ucll.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import org.springframework.stereotype.Component;

@Component
public class FooterComponent extends Div {

    public FooterComponent() {
        setId("footer");
        addClassName("footer");

        Span disclaimer = new Span("Copyrighted © 2024. Do not reuse without authorization.");
        disclaimer.getStyle().set("text-align", "center");
        disclaimer.getStyle().set("font-size", "small");

        add(disclaimer);
    }

}
