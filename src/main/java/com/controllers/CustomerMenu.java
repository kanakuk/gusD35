package com.controllers;

import com.easyschedule.Instance;
import com.people.Customer;
import com.window.Window;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerMenu extends Controller implements Initializable {
    @FXML
    private TextField customerSearchField;
    @FXML
    private TableView customerTable;
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn, addressColumn, divisionColumn, phoneColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTableColumns();
        // Listener works for deleting Customers from allCustomers!!!
        // Does a customer need to change for the event listener to fire or does adding a customer fire the listener?
        Instance.getAllCustomers().addListener(
                (ListChangeListener<? super Customer>) change -> customerTable.setItems(Instance.getAllCustomers()));
        customerTable.setItems(Instance.getAllCustomers());
    }
    @FXML
    private void onSearchClick(ActionEvent actionEvent) {
        ObservableList<Customer> searchResults;
        searchResults = searchCustomers();
        if (searchResults != null) {
            customerTable.setItems(searchResults);
        }
        else openNotifyWindow("No customers found.", actionEvent);
    }
    private ObservableList<Customer> searchCustomers() {
        try {
            int customerId = Integer.parseInt(customerSearchField.getText());
            return FXCollections.observableArrayList(Instance.lookupCustomer(customerId));
        } catch (NumberFormatException e) {
            return Instance.lookupCustomer(customerSearchField.getText());
        }
    }
    @FXML
    private void onViewClick(ActionEvent actionEvent) {
        Window viewCustomer = new Window("view-customer.fxml", "Customer Info");
        viewCustomer.showWindowAndWait(actionEvent);
    }
    @FXML
    private void onAddClick(ActionEvent actionEvent) {
        Window addCustomer = new Window("add-customer.fxml", "Add Customer");
        addCustomer.showWindowAndWait(actionEvent);
    }
    @FXML
    private void onModifyClick(ActionEvent actionEvent) {
        Window modifyCustomer = new Window("modify-customer.fxml", "Edit Customer");
        modifyCustomer.showWindowAndWait(actionEvent);
    }
    @FXML
    private void onDeleteClick(ActionEvent actionEvent) {
        Customer customer = getSelectedCustomer(actionEvent);
        if (customer != null){
            if(!Instance.deleteCustomer(customer)) {
                openNotifyWindow("Deletion of customer with ID " + customer.getId() + "failed!", actionEvent);
            }
        }
    }
    @FXML
    private void onBackClick(ActionEvent actionEvent) {
        Window.changeScene(actionEvent, "main-menu.fxml", "Main Menu");
    }
    private void setTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
    }
    private Customer getSelectedCustomer(ActionEvent actionEvent) {
        Customer selectedCustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            openNotifyWindow("Please select a customer first.", actionEvent);
        }
        actionEvent.consume();
        return selectedCustomer;
    }
}
