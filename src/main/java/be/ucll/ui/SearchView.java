package be.ucll.ui;

import be.ucll.entities.Order;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "search", layout = MainLayout.class)
@PageTitle("Search Orders")
public class SearchView extends VerticalLayout {

    private final TextField productNameField = new TextField("Product naam");
    private final NumberField minAmountField = new NumberField("Minimum bedrag");
    private final NumberField maxAmountField = new NumberField("Maximum bedrag");
    private final NumberField productCountField = new NumberField("Aantal producten");
    private final Checkbox deliveredCheckbox = new Checkbox("Afgeleverd");
    private final EmailField emailField = new EmailField("Email adres");

    private final Grid<Order> orderGrid = new Grid<>(Order.class);

    public SearchView() {
        configureFields();

        Button searchButton = new Button("Zoeken", event -> searchOrders());
        Button clearButton = new Button("Wissen", event -> clearFields());

        Div searchForm = new Div(productNameField, minAmountField, maxAmountField, productCountField, deliveredCheckbox, emailField, searchButton, clearButton);
        searchForm.addClassName("search-form");
        searchForm.getStyle().set("margin-bottom", "20px");

        configureGrid();

        add(searchForm, orderGrid);


        setSpacing(true);
        setPadding(true);
    }

    private void configureFields() {

        emailField.setPlaceholder("example@domain.com");
        emailField.setErrorMessage("Vul een geldig e-mailadres in.");


        minAmountField.setPlaceholder("Min bedrag");
        maxAmountField.setPlaceholder("Max bedrag");
        productCountField.setPlaceholder("Aantal");


        productNameField.addValueChangeListener(event -> {
            if (!event.getValue().isEmpty()) {
                Notification.show("Suggesties voor product: " + event.getValue());
            }
        });
    }

    private void configureGrid() {
        orderGrid.addClassName("order-grid");
        orderGrid.setColumns("id", "customerName", "totalAmount", "delivered");
        orderGrid.getColumnByKey("id").setHeader("Bestel-ID");
        orderGrid.getColumnByKey("customerName").setHeader("Klant");
        orderGrid.getColumnByKey("totalAmount").setHeader("Totaalbedrag");
        orderGrid.getColumnByKey("delivered").setHeader("Afgeleverd?");
    }

    private void searchOrders() {
        List<Order> orders = List.of(
                new Order(1L, "John Doe", 120.50, true),
                new Order(2L, "Jane Smith", 75.99, false)
        );
        orderGrid.setItems(orders);
    }

    private void clearFields() {
        productNameField.clear();
        minAmountField.clear();
        maxAmountField.clear();
        productCountField.clear();
        deliveredCheckbox.clear();
        emailField.clear();
    }



}
