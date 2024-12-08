package be.ucll.ui;


import be.ucll.entities.Order;
import be.ucll.services.OrderService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.Serializable;
import java.util.Optional;

import static java.awt.AWTEventMulticaster.add;


@Route(value = "details/:orderId", layout = MainLayout.class)
@PageTitle("Order Details")
public class DetailView extends VerticalLayout implements BeforeEnterObserver {

    private final OrderService orderService; // Service om data op te halen
    private final Span orderIdLabel = new Span();
    private final Span customerLabel = new Span();
    private final Span productsLabel = new Span();
    private final Span totalLabel = new Span();

    public DetailView(OrderService orderService) {

        this.orderService = orderService;

        VerticalLayout detailsLayout = new VerticalLayout();
        detailsLayout.add(new H3("Order Details"));
        detailsLayout.add(orderIdLabel, customerLabel, productsLabel, totalLabel);

        Button backButton = new Button("Terug", event -> {
            UI.getCurrent().navigate("search");
        });


        add(detailsLayout, backButton);

        setOrderDetails();

    }

    private void setOrderDetails() {
        Optional<String> orderId = UI.getCurrent().getInternals().getActiveViewLocation().getQueryParameters().getParameters().get("orderId")
                .stream().findFirst();
        if (orderId.isPresent()) {
            Order order = orderService.getOrderById(Long.valueOf(orderId.get()));
            orderIdLabel.setText("Bestel-ID: " + order.getId());
            customerLabel.setText("Klantnummer: " + order.getId());
            productsLabel.setText("Aantal Producten: " + order.getProductQuantity());
            totalLabel.setText("Totaal Bedrag: €" + order.getTotalAmount());
        } else {
            orderIdLabel.setText("Bestelling niet gevonden.");
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Haal de parameter "orderId" op uit de URL
        String orderId = event.getRouteParameters().get("orderId").orElse(null);

        if (orderId != null) {
            setOrderDetails();
        } else {
            orderIdLabel.setText("Geen bestelling gevonden.");
        }
    }

}
