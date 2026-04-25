package view;

import controller.TenantController;
import javafx.application.Application;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Tenant;

public class MainApp extends Application {

    private TenantController controller = new TenantController();

    private TableView<Tenant> table;

    private TextField nameField;
    private TextField rentField;
    private TextField utilitiesField;
    private TextField leaseField;
    private TextField searchField;

    @Override
    public void start(Stage stage) {

        Label title = new Label("Multi-Tenant Housing Manager");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        nameField = new TextField();
        nameField.setPromptText("Tenant Name");

        rentField = new TextField();
        rentField.setPromptText("Rent");

        utilitiesField = new TextField();
        utilitiesField.setPromptText("Utilities");

        leaseField = new TextField();
        leaseField.setPromptText("Lease Months");

        searchField = new TextField();
        searchField.setPromptText("Search tenant by name");

        HBox inputBox = new HBox(10, nameField, rentField, utilitiesField, leaseField);

        Button addButton = new Button("Add Tenant");
        Button editButton = new Button("Edit Selected");
        Button deleteButton = new Button("Delete Selected");
        Button clearButton = new Button("Clear");

        HBox buttonBox = new HBox(10, addButton, editButton, deleteButton, clearButton);

        table = createTable();

        FilteredList<Tenant> filteredTenants = new FilteredList<>(controller.getTenants(), tenant -> true);

        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredTenants.setPredicate(tenant -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return tenant.getName().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        table.setItems(filteredTenants);

        addButton.setOnAction(e -> addTenant());
        editButton.setOnAction(e -> editTenant());
        deleteButton.setOnAction(e -> deleteTenant());
        clearButton.setOnAction(e -> clearFields());

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selected) -> {
            if (selected != null) {
                nameField.setText(selected.getName());
                rentField.setText(String.valueOf(selected.getRent()));
                utilitiesField.setText(String.valueOf(selected.getUtilities()));
                leaseField.setText(String.valueOf(selected.getLeaseMonths()));
            }
        });

        VBox root = new VBox(12);
        root.setStyle("-fx-padding: 20; -fx-background-color: #f4f6f8;");
        root.getChildren().addAll(
                title,
                inputBox,
                buttonBox,
                searchField,
                table
        );

        Scene scene = new Scene(root, 900, 550);

        stage.setTitle("Multi-Tenant Housing Manager");
        stage.setScene(scene);
        stage.show();
    }

    private TableView<Tenant> createTable() {
        TableView<Tenant> tableView = new TableView<>();

        TableColumn<Tenant, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<Tenant, Number> rentColumn = new TableColumn<>("Rent");
        rentColumn.setCellValueFactory(data -> data.getValue().rentProperty());

        TableColumn<Tenant, Number> utilitiesColumn = new TableColumn<>("Utilities");
        utilitiesColumn.setCellValueFactory(data -> data.getValue().utilitiesProperty());

        TableColumn<Tenant, Number> leaseColumn = new TableColumn<>("Lease Months");
        leaseColumn.setCellValueFactory(data -> data.getValue().leaseMonthsProperty());

        tableView.getColumns().addAll(nameColumn, rentColumn, utilitiesColumn, leaseColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(320);

        return tableView;
    }

    private void addTenant() {
        try {
            String name = nameField.getText();
            double rent = Double.parseDouble(rentField.getText());
            double utilities = Double.parseDouble(utilitiesField.getText());
            int leaseMonths = Integer.parseInt(leaseField.getText());

            if (name.isEmpty()) {
                showAlert("Tenant name cannot be empty.");
                return;
            }

            Tenant tenant = new Tenant(name, rent, utilities, leaseMonths);
            controller.addTenant(tenant);

            clearFields();

        } catch (NumberFormatException e) {
            showAlert("Please enter valid numbers for rent, utilities, and lease months.");
        }
    }

    private void editTenant() {
        Tenant selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Please select a tenant to edit.");
            return;
        }

        try {
            String name = nameField.getText();
            double rent = Double.parseDouble(rentField.getText());
            double utilities = Double.parseDouble(utilitiesField.getText());
            int leaseMonths = Integer.parseInt(leaseField.getText());

            controller.updateTenant(selected, name, rent, utilities, leaseMonths);

            table.refresh();
            clearFields();

        } catch (NumberFormatException e) {
            showAlert("Please enter valid numbers before editing.");
        }
    }

    private void deleteTenant() {
        Tenant selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Please select a tenant to delete.");
            return;
        }

        controller.removeTenant(selected);
        clearFields();
    }

    private void clearFields() {
        nameField.clear();
        rentField.clear();
        utilitiesField.clear();
        leaseField.clear();
        table.getSelectionModel().clearSelection();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}