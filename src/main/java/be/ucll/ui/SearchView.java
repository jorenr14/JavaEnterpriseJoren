package be.ucll.ui;

import be.ucll.entities.Order;
import be.ucll.services.MailService;
import be.ucll.services.OrderService;
import be.ucll.spring.JmsProducer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "search", layout = MainLayout.class)
@PageTitle("Search Orders")
public class SearchView extends VerticalLayout {

    private final ComboBox<String> productNameField = new ComboBox<>("Product naam");
    private final NumberField minAmountField = new NumberField("Minimum bedrag");
    private final NumberField maxAmountField = new NumberField("Maximum bedrag");
    private final Checkbox deliveredCheckbox = new Checkbox("Afgeleverd");
    private final NumberField productCountField = new NumberField("Aantal producten");
    private final EmailField emailField = new EmailField("Email adres");

    private final Grid<Order> orderGrid = new Grid<>(Order.class, false);
    private final OrderService orderService;

    @Autowired
    private MailService emailService;

    @Autowired
    private JmsProducer jmsProducer;

    public SearchView(OrderService orderService) {

        this.orderService = orderService;

        configureBinder();
        configureFields();
        configureGrid();

        Button searchButton = new Button("Zoeken", event -> searchOrders());
        Button clearButton = new Button("Wissen", event -> clearFields());
        Button emailButton = new Button("Stuur Email", event -> sendEmail());


        productNameField.setAllowCustomValue(false);
        productNameField.setItems("Muis", "Toetsenbord", "Laptop");
        productNameField.setPlaceholder("Selecteer een product...");
        productNameField.addValueChangeListener(event -> searchOrders());


        Div searchForm = new Div(productNameField, minAmountField, maxAmountField,
                productCountField, emailField, deliveredCheckbox,
                searchButton, clearButton, emailButton);

        searchForm.addClassName("search-form");
        searchForm.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(3, 1fr)")
                .set("gap", "10px")
                .set("margin-bottom", "20px");


        orderGrid.addItemClickListener(event -> {
            Long orderId = event.getItem().getId();
            getUI().ifPresent(ui -> ui.navigate("details/" + orderId));
        });

        add(searchForm, orderGrid);
        add(new FooterComponent());
        loadOrders();
    }

    private void configureBinder() {
        Binder<Order> orderBinder = new Binder<>(Order.class);
        orderBinder.forField(minAmountField)
                .withValidator(value -> value == null || value >= 0, "Minimum bedrag moet positief zijn");
        orderBinder.forField(maxAmountField)
                .withValidator(value -> value == null || value >= 0, "Maximum bedrag moet positief zijn");
        orderBinder.forField(emailField)
                .withValidator(email -> email.contains("@") && email.contains("."), "Vul een geldig e-mailadres in");
    }

    private void configureFields() {
        emailField.setPlaceholder("example@domain.com");
        emailField.setErrorMessage("Vul een geldig e-mailadres in.");
        minAmountField.setPlaceholder("Min bedrag");
        maxAmountField.setPlaceholder("Max bedrag");
        productCountField.setPlaceholder("Aantal");

        minAmountField.addBlurListener(event -> validateField(minAmountField));
        maxAmountField.addBlurListener(event -> validateField(maxAmountField));
        productCountField.addBlurListener(event -> validateField(productCountField));
    }

    private void searchProducts(String searchTerm) {
        List<String> suggestions = orderService.findProductByName(searchTerm);
        productNameField.setItems(suggestions);
    }

    private void configureGrid() {
        orderGrid.addColumn(Order::getId).setHeader("ID");
        orderGrid.addColumn(Order::getCustomerName).setHeader("Klant");
        orderGrid.addColumn(order -> order.getProducts().size()).setHeader("Aantal Producten");
        orderGrid.addColumn(Order::getTotalAmount).setHeader("Totaalbedrag (€)");
        orderGrid.addColumn(Order::isDelivered).setHeader("Afgeleverd");

        orderGrid.addColumn(order ->
                order.getProducts().stream()
                        .map(product -> product.getName() + " (€" + product.getPrice() + ")")
                        .collect(Collectors.joining(", "))
        ).setHeader("Product(en)");
    }

    private void loadOrders() {
        orderGrid.setItems(orderService.findAll());
    }

    private void searchOrders() {
        loadOrders();

        String productName = productNameField.getValue();
        Double minAmount = minAmountField.getValue();
        Double maxAmount = maxAmountField.getValue();
        Boolean delivered = deliveredCheckbox.getValue();
        Double productCount = productCountField.getValue();
        String email = emailField.getValue();

        List<Order> orders = orderService.findOrders(
                productName, minAmount, maxAmount, delivered, email
        );


        System.out.println("Aantal gevonden orders: " + orders.size());
        orders.forEach(order -> System.out.println("Matched Order: " + order.getCustomerName() + ", Totaal: " + order.getTotalAmount()));


        orderGrid.setItems(orders);

        if (orders.isEmpty()) {
            Notification.show("Geen resultaten gevonden.");
        }
    }


    private void sendEmail() {
        List<Order> selectedOrders = orderGrid.getListDataView().getItems().toList();

        if (selectedOrders.isEmpty()) {
            Notification.show("Geen bestellingen om te verzenden.");
            return;
        }


        List<String> gridData = selectedOrders.stream()
                .map(order -> {
                    String productDetails = order.getProducts().stream()
                            .map(product -> product.getName() + " (€" + product.getPrice() + ")")
                            .collect(Collectors.joining(", "));
                    return String.format("%s: €%.2f (%s)",
                            order.getCustomerName(),
                            order.getTotalAmount(),
                            productDetails);
                })
                .toList();

        // Controleer e-mailadres
        String email = emailField.getValue();
        if (email == null || email.isEmpty() || !email.contains("@")) {
            Notification.show("Vul een geldig e-mailadres in.");
            return;
        }

        // Verstuur e-mail
        jmsProducer.sendMessage(email, gridData);
        emailService.sendmail(email, gridData);

        Notification.show("E-mail wordt asynchroon verzonden naar " + email + "!");
    }


    private void clearFields() {
        productNameField.clear();
        minAmountField.clear();
        maxAmountField.clear();
        productCountField.clear();
        deliveredCheckbox.clear();
        emailField.clear();
        loadOrders();
    }

    private void validateField(NumberField field) {
        if (field.isInvalid()) {
            field.setInvalid(true);
        } else {
            field.setInvalid(false);
        }
    }
}
