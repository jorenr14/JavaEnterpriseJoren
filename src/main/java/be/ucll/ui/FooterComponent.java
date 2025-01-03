package be.ucll.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "vaadin-ui", proxyMode = ScopedProxyMode.TARGET_CLASS)
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
