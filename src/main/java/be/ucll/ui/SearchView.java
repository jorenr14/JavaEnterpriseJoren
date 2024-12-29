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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Search Orders")
public class SearchView extends VerticalLayout {

    private final ComboBox<String> productNameField = new ComboBox<>("Product naam");
    private final NumberField minAmountField = new NumberField("Minimum bedrag");
    private final NumberField maxAmountField = new NumberField("Maximum bedrag");
    private final NumberField productCountField = new NumberField("Aantal producten");
    private final Checkbox deliveredCheckbox = new Checkbox("Afgeleverd");
    private final EmailField emailField = new EmailField("Email adres");

    private final Grid<Order> orderGrid = new Grid<>(Order.class);
    private final OrderService orderService;
    private Binder<Order> orderBinder= new Binder<>(Order.class);

    @Autowired
    private MailService emailService;
    @Autowired
    private JmsProducer jmsProducer;

    public SearchView(OrderService orderService) {



        this.orderService = orderService;
        configureBinder();
        configureFields();

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


        Div searchForm = new Div(productNameField, minAmountField, maxAmountField, productCountField, deliveredCheckbox, emailField, searchButton, clearButton, emailButton);
        searchForm.addClassName("search-form");
        searchForm.getStyle().set("margin-bottom", "20px");

        orderGrid.addItemClickListener(event -> {
            Long orderId = event.getItem().getId();
            getUI().ifPresent(ui -> ui.navigate("details/" + orderId));
        });

        //configureGrid();
        loadOrders();

        add(searchForm, orderGrid);


        setSpacing(true);
        setPadding(true);
    }

    //online gevonden nog uitdokteren
    private void configureBinder(){
        orderBinder.forField(minAmountField)
                .withValidator(value -> value == null || value >= 0, "Minimum bedrag moet positief zijn");


        orderBinder.forField(maxAmountField)
                .withValidator(value -> value == null || value >= 0, "Maximum bedrag moet positief zijn");


        orderBinder.forField(emailField)
                .withValidator(email -> email.contains("@") && email.contains("."), "Voer een geldig e-mailadres in");

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
                List<String> suggestions = orderService.findProductNames(searchTerm);
                productNameField.setItems(suggestions);
            }
        });
    }

    private void configureGrid() {
        orderGrid.addColumn(Order::getId).setHeader("ID");
        orderGrid.addColumn(Order::getCustomerName).setHeader("Klant");
        orderGrid.addColumn(order -> order.getProducts().size()).setHeader("# Producten");
        orderGrid.addColumn(Order::isDelivered).setHeader("Afgeleverd?");
        orderGrid.addColumn(Order::getTotalAmount).setHeader("Totaalbedrag (€)");


    }

        private void loadOrders(){
        orderGrid.setItems(orderService.findAll());
    }
    private void searchOrders(){
        // Valideer invoervelden
        if (!orderBinder.validate().isOk()) {
            Notification.show("Er zijn validatiefouten in de invoervelden.");
            return;
        }

        // Controleer of criteria zijn ingevuld
        if (productNameField.isEmpty() && minAmountField.isEmpty() && maxAmountField.isEmpty() &&
                productCountField.isEmpty() && deliveredCheckbox.isEmpty() && emailField.isEmpty()) {
            Notification.show("Geef ten minste één zoekcriteria op.");
            return;
        }

        // Ophalen zoekcriteria
        String productName = productNameField.getValue();
        Double minAmount = minAmountField.getValue();
        Double maxAmount = maxAmountField.getValue();
        Boolean delivered = deliveredCheckbox.getValue();
        String email = emailField.getValue();

        // Query de service voor orders die overeenkomen met de criteria
        List<Order> orders = orderService.findOrders(productName, minAmount, maxAmount, delivered, email);
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


    private void validateField(NumberField field) {
        if (!orderBinder.validate().isOk()) {
            field.setInvalid(true);
        } else {
            field.setInvalid(false);
        }
    }






}
