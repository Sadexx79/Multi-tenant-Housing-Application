package view;

import controller.TenantController;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Tenant;
import util.FileHandler;

public class MainApp extends Application {

    private TenantController controller = new TenantController();

    private TableView<Tenant> table;
    private BarChart<String, Number> chart;

    private TextField nameField;
    private TextField rentField;
    private TextField utilitiesField;
    private TextField leaseField;
    private TextField searchField;

    private Label totalTenantsLabel;

    private boolean darkMode = true;

    @Override
    public void start(Stage stage) {

        Label title = new Label("Multi-Tenant Housing Manager");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        totalTenantsLabel = new Label();
        totalTenantsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

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

        String fieldStyle =
                "-fx-background-color: #2a2a3d;" +
                "-fx-text-fill: white;" +
                "-fx-prompt-text-fill: #aaaaaa;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-border-color: #3f3f5a;" +
                "-fx-padding: 8;";

        nameField.setStyle(fieldStyle);
        rentField.setStyle(fieldStyle);
        utilitiesField.setStyle(fieldStyle);
        leaseField.setStyle(fieldStyle);
        searchField.setStyle(fieldStyle);

        HBox inputBox = new HBox(10, nameField, rentField, utilitiesField, leaseField);

        Button addButton = new Button("Add Tenant");
        Button editButton = new Button("Edit Selected");
        Button deleteButton = new Button("Delete Selected");
        Button clearButton = new Button("Clear Fields");
        Button clearAllButton = new Button("Clear All");
        Button exportButton = new Button("Export CSV");
        Button themeButton = new Button("Toggle Theme");

        String buttonStyle =
                "-fx-background-color: #3b82f6;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 10 16 10 16;" +
                "-fx-cursor: hand;";

        addButton.setStyle(buttonStyle);
        editButton.setStyle(buttonStyle);
        deleteButton.setStyle(buttonStyle);
        clearButton.setStyle(buttonStyle);
        clearAllButton.setStyle(buttonStyle);
        exportButton.setStyle(buttonStyle);
        themeButton.setStyle(buttonStyle);

        HBox buttonBox = new HBox(
                10,
                addButton,
                editButton,
                deleteButton,
                clearButton,
                clearAllButton,
                exportButton,
                themeButton
        );

        table = createTable();
        chart = createChart();

        table.setStyle(
                "-fx-background-color: #1f1f2e;" +
                "-fx-control-inner-background: #1f1f2e;" +
                "-fx-table-cell-border-color: #2f2f45;" +
                "-fx-text-background-color: white;" +
                "-fx-selection-bar: #3b82f6;"
        );

        chart.setStyle(
                "-fx-background-color: #1f1f2e;" +
                "-fx-text-fill: white;"
        );

        FilteredList<Tenant> filteredTenants =
                new FilteredList<>(controller.getTenants(), tenant -> true);

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
        clearAllButton.setOnAction(e -> clearAllTenants());
        exportButton.setOnAction(e -> FileHandler.export(controller.getTenants(), stage));

        VBox root = new VBox(12);
        root.setStyle(
                "-fx-padding: 20;" +
                "-fx-background-color: linear-gradient(to bottom, #1e1e2f, #121212);"
        );

        themeButton.setOnAction(e -> toggleTheme(root, title));

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selected) -> {
            if (selected != null) {
                nameField.setText(selected.getName());
                rentField.setText(String.valueOf(selected.getRent()));
                utilitiesField.setText(String.valueOf(selected.getUtilities()));
                leaseField.setText(String.valueOf(selected.getLeaseMonths()));
            }
        });

        root.getChildren().addAll(
                title,
                totalTenantsLabel,
                inputBox,
                buttonBox,
                searchField,
                table,
                chart
        );

        updateChart();
        updateTotalTenantsLabel();

        Scene scene = new Scene(root, 1050, 780);

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

        TableColumn<Tenant, Number> totalColumn = new TableColumn<>("Total Monthly Payment");
        totalColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(
                        data.getValue().getRent() + data.getValue().getUtilities()
                )
        );

        tableView.getColumns().addAll(
                nameColumn,
                rentColumn,
                utilitiesColumn,
                leaseColumn,
                totalColumn
        );

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(280);

        return tableView;
    }

    private BarChart<String, Number> createChart() {

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Tenant");
        yAxis.setLabel("Rent Amount");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Rent Per Tenant");
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(300);

        return barChart;
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

            if (rent < 0 || utilities < 0 || leaseMonths <= 0) {
                showAlert("Rent and utilities cannot be negative. Lease months must be greater than 0.");
                return;
            }

            Tenant tenant = new Tenant(name, rent, utilities, leaseMonths);
            controller.addTenant(tenant);

            clearFields();
            updateChart();
            updateTotalTenantsLabel();

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

            if (name.isEmpty()) {
                showAlert("Tenant name cannot be empty.");
                return;
            }

            if (rent < 0 || utilities < 0 || leaseMonths <= 0) {
                showAlert("Rent and utilities cannot be negative. Lease months must be greater than 0.");
                return;
            }

            controller.updateTenant(selected, name, rent, utilities, leaseMonths);

            table.refresh();
            clearFields();
            updateChart();
            updateTotalTenantsLabel();

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

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Confirmation");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete this tenant?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            controller.removeTenant(selected);
            clearFields();
            updateChart();
            updateTotalTenantsLabel();
        }
    }

    private void clearAllTenants() {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Clear All Confirmation");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to remove all tenants?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            controller.clearAllTenants();
            clearFields();
            updateChart();
            updateTotalTenantsLabel();
        }
    }

    private void clearFields() {
        nameField.clear();
        rentField.clear();
        utilitiesField.clear();
        leaseField.clear();
        table.getSelectionModel().clearSelection();
    }

    private void updateChart() {

        chart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Tenant tenant : controller.getTenants()) {
            series.getData().add(
                    new XYChart.Data<>(tenant.getName(), tenant.getRent())
            );
        }

        chart.getData().add(series);
    }

    private void updateTotalTenantsLabel() {
        totalTenantsLabel.setText("Total Tenants: " + controller.getTenants().size());
    }

    private void toggleTheme(VBox root, Label title) {

        if (darkMode) {

            root.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;");

            table.setStyle("");
            chart.setStyle("");

            String lightFieldStyle =
                    "-fx-background-color: white;" +
                    "-fx-text-fill: black;" +
                    "-fx-prompt-text-fill: gray;" +
                    "-fx-background-radius: 8;" +
                    "-fx-border-radius: 8;" +
                    "-fx-border-color: #cccccc;" +
                    "-fx-padding: 8;";

            nameField.setStyle(lightFieldStyle);
            rentField.setStyle(lightFieldStyle);
            utilitiesField.setStyle(lightFieldStyle);
            leaseField.setStyle(lightFieldStyle);
            searchField.setStyle(lightFieldStyle);

            title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: black;");
            totalTenantsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");

            darkMode = false;

        } else {

            root.setStyle(
                    "-fx-padding: 20;" +
                    "-fx-background-color: linear-gradient(to bottom, #1e1e2f, #121212);"
            );

            table.setStyle(
                    "-fx-background-color: #1f1f2e;" +
                    "-fx-control-inner-background: #1f1f2e;" +
                    "-fx-table-cell-border-color: #2f2f45;" +
                    "-fx-text-background-color: white;" +
                    "-fx-selection-bar: #3b82f6;"
            );

            chart.setStyle(
                    "-fx-background-color: #1f1f2e;" +
                    "-fx-text-fill: white;"
            );

            String darkFieldStyle =
                    "-fx-background-color: #2a2a3d;" +
                    "-fx-text-fill: white;" +
                    "-fx-prompt-text-fill: #aaaaaa;" +
                    "-fx-background-radius: 8;" +
                    "-fx-border-radius: 8;" +
                    "-fx-border-color: #3f3f5a;" +
                    "-fx-padding: 8;";

            nameField.setStyle(darkFieldStyle);
            rentField.setStyle(darkFieldStyle);
            utilitiesField.setStyle(darkFieldStyle);
            leaseField.setStyle(darkFieldStyle);
            searchField.setStyle(darkFieldStyle);

            title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
            totalTenantsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

            darkMode = true;
        }
    }

    private void showAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}