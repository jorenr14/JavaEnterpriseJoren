package be.ucll.ui;


import be.ucll.entities.Order;
import be.ucll.services.OrderService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Optional;

import static java.awt.AWTEventMulticaster.add;


@Route(value = "details", layout = MainLayout.class)
@PageTitle("Order Details")
public class DetailView extends VerticalLayout implements HasUrlParameter<Long> {

    private final OrderService orderService; // Service om data op te halen
    private Order order;

    @Autowired
    public DetailView(OrderService orderService) {
        this.orderService = orderService;

        setPadding(true);
        setSpacing(true);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long orderId) {
        if (orderId != null) {
            // Haal het order op basis van ID
            Optional<Order> optionalOrder = orderService.getOrderById(orderId);
            if (optionalOrder.isPresent()) {
                this.order = optionalOrder.get();
                setupLayout();
            } else {
                add(new H2("Bestelling niet gevonden."));
            }
        } else {
            add(new H2("Geen bestelling geselecteerd."));
        }
    }

    private void setupLayout() {
        add(new H2("Bestelling ID: " + order.getId()));
        add(new Div("Klant: " + order.getCustomerName()));
        add(new Div("Datum: " + order.getOrderDate()));
        add(new Div("Totaal: €" + order.getTotalAmount()));
        add(new Div("Afgeleverd: " + (order.isDelivered() ? "Ja" : "Nee")));

        // Voeg terugknop toe
        Button backButton = new Button("Terug naar overzicht", event -> getUI().ifPresent(ui -> ui.navigate(SearchView.class)));
        add(backButton);
    }

}
