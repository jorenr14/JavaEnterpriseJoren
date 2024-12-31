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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

@Route(value = "", layout = MainLayout.class)
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
    private Binder<Order> orderBinder = new Binder<>(Order.class);

    @Autowired
    private MailService emailService;

    @Autowired
    private JmsProducer jmsProducer;

    public SearchView(OrderService orderService) {

        this.orderService = orderService;

        configureBinder();
        configureFields();
        configureGrid();

        // Knoppen aanmaken
        Button searchButton = new Button("Zoeken", event -> searchOrders());
        Button clearButton = new Button("Wissen", event -> clearFields());
        Button emailButton = new Button("Stuur Email", event -> {
            List<Order> selectedOrders = orderGrid.getListDataView().getItems().toList();

            if (selectedOrders.isEmpty()) {
                Notification.show("Selecteer minstens één bestelling.");
                return;
            }

//            List<Long> productIds = selectedOrders.stream()
//                    .flatMap(order -> order.getProducts().stream().map(product -> product.getId()))
//                    .toList();
            List<String> gridData = selectedOrders.stream()
                    .map(order -> String.format("<td>%d</td><td>%s</td><td>%.2f</td><td>%s</td>",
                            order.getId(),
                            order.getCustomerName(),
                            order.getTotalAmount(),
                            order.isDelivered() ? "Ja" : "Nee"))
                    .toList();

            String email = emailField.getValue();
            if (email == null || email.isEmpty() || !email.contains("@")) {
                Notification.show("Vul een geldig e-mailadres in.");
                return;
            }

            // Verstuur asynchroon via JMS
            try {
                jmsProducer.sendMessage(email,gridData);
                emailService.sendmail(email, gridData);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            Notification.show("E-mail wordt asynchroon verzonden naar " + email + "!");
        });

        productNameField.setAllowCustomValue(true);
        productNameField.setPlaceholder("Typ productnaam...");
        productNameField.addCustomValueSetListener(event -> {
            String customValue = event.getDetail();
            productNameField.setValue(customValue); // Stelt custom waarde in
            Notification.show("Geselecteerde waarde: " + customValue);
        });

        productNameField.setItems("Muis", "Toetsenbord", "Laptop");


        productNameField.addValueChangeListener(event -> {
            String searchTerm = event.getValue(); // Haal zoekterm op
            if (searchTerm != null && !searchTerm.isEmpty()) {

                if (!searchTerm.equals(productNameField.getValue())) {

                    List<String> suggestions = orderService.findProductByName(searchTerm);
                    System.out.println("UI ComboBox Results: " + suggestions);
                    productNameField.setItems(suggestions);
                }
            }
        });



        Div searchForm = new Div(productNameField, minAmountField, maxAmountField, productCountField, deliveredCheckbox, emailField, searchButton, clearButton, emailButton);
        searchForm.addClassName("search-form");
        searchForm.getStyle().set("margin-bottom", "20px");

        orderGrid.addItemClickListener(event -> {
            Long orderId = event.getItem().getId();
            getUI().ifPresent(ui -> ui.navigate("details/" + orderId));
        });

        add(searchForm, orderGrid);
        loadOrders(); // Laad initiële data
    }

    private void configureBinder() {
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
        emailField.addBlurListener(event -> {
            String emailRegex = "^[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$";
            if (!emailField.getValue().matches(emailRegex)) {
                emailField.setInvalid(true);
            } else {
                emailField.setInvalid(false);
            }
        });
        productCountField.addBlurListener(event -> {
            Double value = productCountField.getValue();
            if (value == null || value < 0 || value % 1 != 0) {
                productCountField.setErrorMessage("Aantal moet een geheel getal zijn.");
                productCountField.setInvalid(true);
            } else {
                productCountField.setInvalid(false);
            }
        });

        productNameField.setAllowCustomValue(true);
        productNameField.setPlaceholder("Typ productnaam...");
        productNameField.addCustomValueSetListener(event -> {
            String customValue = event.getDetail();
            productNameField.setValue(customValue);
            Notification.show("Geselecteerde waarde: " + customValue);
        });
        productNameField.addValueChangeListener(event -> {
            String searchTerm = event.getValue();
            if (searchTerm != null && !searchTerm.isEmpty()) {
                List<String> suggestions = orderService.findProductByName(searchTerm);
                productNameField.setItems(suggestions);
            }
        });

        productNameField.addCustomValueSetListener(event -> {
            String customValue = event.getDetail();
            productNameField.setValue(customValue); // Stelt de custom waarde in als geselecteerde waarde
            Notification.show("Geselecteerde waarde: " + customValue);


            searchOrders();
        });
    }

    private void configureGrid() {
        orderGrid.addColumn(Order::getId).setHeader("ID");
        orderGrid.addColumn(Order::getCustomerName).setHeader("Klant");
        orderGrid.addColumn(order -> order.getProducts().size()).setHeader("aantal Producten");
        orderGrid.addColumn(Order::isDelivered).setHeader("Afgeleverd");
        orderGrid.addColumn(Order::getTotalAmount).setHeader("Totaalbedrag (€)");

        // Producten weergeven
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
        List<Order> orders = orderService.findOrders(
                productNameField.getValue(),
                minAmountField.getValue(),
                maxAmountField.getValue(),
                deliveredCheckbox.getValue(),
                emailField.getValue()
        );
        orderGrid.setItems(orders);
    }

    private void sendEmail() {
        List<Order> selectedOrders = orderGrid.getListDataView().getItems().toList();

        if (selectedOrders.isEmpty()) {
            Notification.show("Geen bestellingen om te verzenden.");
            return;
        }

        List<String> gridData = selectedOrders.stream()
                .map(order -> String.format("%s: €%.2f", order.getCustomerName(), order.getTotalAmount()))
                .toList();

        String email = emailField.getValue();
        if (email == null || email.isEmpty() || !email.contains("@")) {
            Notification.show("Vul een geldig e-mailadres in.");
            return;
        }

        try {
            jmsProducer.sendMessage(email, gridData);
            emailService.sendmail(email, gridData);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        Notification.show("E-mail wordt asynchroon verzonden naar " + email + "!");
    }

    private void clearFields() {
        productNameField.clear();
        minAmountField.clear();
        maxAmountField.clear();
        deliveredCheckbox.clear();
        emailField.clear();
    }
    private void validateField(NumberField field) {
        if (!orderBinder.validate().isOk()) {
            field.setInvalid(true);
        } else {
            field.setInvalid(false);
        }
    }
}
