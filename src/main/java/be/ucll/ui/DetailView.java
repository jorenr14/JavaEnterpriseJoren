package be.ucll.ui;

import be.ucll.entities.Order;
import be.ucll.entities.Product;
import be.ucll.entities.ProductDTO;
import be.ucll.services.OrderService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.stream.Collectors;

@Route(value = "details", layout = MainLayout.class)
@PageTitle("Order Details")
public class DetailView extends VerticalLayout implements HasUrlParameter<Long> {

    private final OrderService orderService; // Service om data op te halen
    private Order order;

    // Dynamische tabel voor producten
    private final Grid<ProductDTO> productGrid = new Grid<>(ProductDTO.class);

    @Autowired
    public DetailView(OrderService orderService) {
        this.orderService = orderService;

        // Layout instellingen
        setPadding(true);
        setSpacing(true);
        setSizeFull();


    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long orderId) {
        if (orderId != null) {
            // Haal bestelling op basis van ID
            Optional<Order> optionalOrder = orderService.getOrderById(orderId);
            if (optionalOrder.isPresent()) {
                this.order = optionalOrder.get();
                setupLayout(); // Bouw de UI op basis van de bestelling
            } else {
                add(new H2("Bestelling niet gevonden."));
            }
        } else {
            add(new H2("Geen bestelling geselecteerd."));
        }
    }

    private void setupLayout() {
        // Titel
        H2 title = new H2("Bestelling ID: " + order.getId());
        add(title);

        // Bestellingsgegevens - Horizontale layout
        HorizontalLayout detailsLayout = new HorizontalLayout();
        detailsLayout.setWidthFull();
        detailsLayout.setSpacing(true);

        // Bestellingsinfo direct toevoegen zonder infoDiv
        detailsLayout.add(new Div("Klantnaam: " + order.getCustomerName()));
        detailsLayout.add(new Div("Datum: " + order.getOrderDate().toString()));
        detailsLayout.add(new Div("Totaal (€): " + String.format("%.2f", order.getTotalAmount())));
        detailsLayout.add(new Div("Afgeleverd: " + (order.isDelivered() ? "Ja" : "Nee")));
        detailsLayout.add(new Div("# Producten: " + order.getProducts().size()));

        // Voeg gegevens toe aan de layout
        add(detailsLayout);

        // Dynamische tabel voor producten
        productGrid.setItems(order.getProducts().stream()
                .map(product -> new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice()))
                .collect(Collectors.toList()));

        // Voeg de product grid toe
        add(productGrid);

        // Terugknop
        Button backButton = new Button("Terug naar overzicht", event -> getUI().ifPresent(ui -> ui.navigate(SearchView.class)));
        backButton.getStyle().set("margin-top", "10px");
        backButton.getStyle().set("background-color", "#007bff");
        backButton.getStyle().set("color", "white");
        backButton.getStyle().set("border-radius", "5px");
        backButton.getStyle().set("padding", "10px 20px");
        add(backButton);
    }

    private void configureProductGrid() {

        productGrid.addColumn(ProductDTO::getId).setHeader("Product ID");
        productGrid.addColumn(ProductDTO::getName).setHeader("Naam");
        productGrid.addColumn(ProductDTO::getDescription).setHeader("Beschrijving");
        productGrid.addColumn(ProductDTO::getPrice).setHeader("Prijs (€)");

        productGrid.setSizeFull();
    }

}

