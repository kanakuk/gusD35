package com.controllers;

import com.easyschedule.Appointment;
import com.easyschedule.Instance;
import com.people.Customer;
import com.window.Window;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class CalendarView extends Controller implements Initializable {
    private Customer customer;
    @FXML
    private Label customerLabel;
    @FXML
    private TableColumn<Appointment, Integer> idColumn, customerColumn, userColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn, descriptionColumn, locationColumn, contactColumn, typeColumn;
    @FXML
    private TableColumn<Appointment, String> startColumn, endColumn;
    @FXML
    private TableView<Appointment> appointmentsTable;
    private ObservableList<Appointment> associatedAppointments;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab allAppointments, monthAppointments, weekAppointments;
    @FXML
    private TextField appointmentSearchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTableColumns();
    }
    private void setTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("UserId"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("ContactId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("FormattedStartDate"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("FormattedEndDate"));
    }
    protected void setCustomer(Customer customer) {
        this.customer = customer;
        associatedAppointments = customer.getAssociatedAppointments();
        associatedAppointments.addListener((ListChangeListener<? super Appointment>) change -> updateTable());
        customerLabel.setText("Customer: " + customer.getName());
        updateTable();
    }
    @FXML
    private void onSearchClick(ActionEvent actionEvent) {
        ObservableList<Appointment> returnList = FXCollections.observableArrayList();
        String searchParam = appointmentSearchField.getText();
        if (searchParam.length() == 0) {
            updateTable();
        }
        else {
            try {
                Integer id = Integer.parseInt(searchParam);
                for (Appointment appointment : associatedAppointments) {
                    if (appointment.getAppointmentId() == id) {
                        returnList.add(appointment);
                    }
                }
                if (returnList.isEmpty()) {
                    openNotifyWindow(actionEvent, "No appointment with that ID found!");
                } else appointmentsTable.setItems(returnList);
            } catch (NumberFormatException ignored) {
                openNotifyWindow(actionEvent, "Appointment ID only allowed");
            }
        }
    }
    @FXML
    private void onAddClick(ActionEvent actionEvent) {
        Window addAppointment = new Window("appointment.fxml", "Add Appointment");
        AppointmentManagement controller = (AppointmentManagement) addAppointment.getController();
        controller.setCustomer(customer);
        addAppointment.showWindowAndWait(actionEvent);
    }
    @FXML
    private void onModifyClick(ActionEvent actionEvent) {
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (appointment != null) {
            Window modifyAppointment = new Window("appointment.fxml", "Modify Appointment");
            AppointmentManagement controller = (AppointmentManagement) modifyAppointment.getController();
            controller.setCustomer(customer);
            controller.setAppointment(appointment);
            modifyAppointment.showWindowAndWait(actionEvent);
            appointmentsTable.refresh();
        }
        else {
            openNotifyWindow(actionEvent, "Please select an appointment");
        }
    }
    @FXML
    private void onDeleteClick(ActionEvent actionEvent) {
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (appointment != null) {
            if (!Instance.deleteAppointment(appointment)) {
                associatedAppointments.remove(appointment);
                openNotifyWindow(actionEvent, "Appointment with ID " + appointment.getAppointmentId() + " successfully deleted.");
            }
            else {
                openNotifyWindow(actionEvent, "Appointment with ID " + appointment.getAppointmentId() + " unsuccessfully deleted.");
            }
        }
        else {
            openNotifyWindow(actionEvent, "Please select an appointment");
        }
    }
    @FXML
    private void updateTable() {
        ZonedDateTime now = ZonedDateTime.now(Instance.SYSTEMZONEID);
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        if (selectedTab.equals(allAppointments)) {
            //Get all associated appointments
            appointmentsTable.setItems(customer.getAssociatedAppointments());
        }
        else if (selectedTab.equals(monthAppointments)) {
            // Get all associated appointments one Month after today.

            ZonedDateTime before = now.plusMonths(1);
            appointmentsTable.setItems(getAppointments(now, before));
        }
        else if (selectedTab.equals(weekAppointments)) {
            // Get all associated appointments 7 days after today.
            ZonedDateTime before = now.plusWeeks(1);
            appointmentsTable.setItems(getAppointments(now, before));
        }
    }
    private ObservableList<Appointment> getAppointments(ZonedDateTime after, ZonedDateTime before) {
        ObservableList<Appointment> returnList = FXCollections.observableArrayList();

        for (Appointment appointment : associatedAppointments){
            ZonedDateTime appointmentDate = appointment.getStartDate();
            if (appointmentDate.isAfter(after) && appointmentDate.isBefore(before)) {
                returnList.add(appointment);
            }
        }
        return returnList;
    }
/*
    private ObservableList<Appointment> getAppointments(){
        ObservableList<Appointment> returnList = FXCollections.observableArrayList();
        LocalDateTime now = LocalDateTime.now();

        for (Appointment appointment : associatedAppointments){
            LocalDateTime appointmentDate = appointment.getStartDate();
            if (appointmentDate.isAfter(now)) {
                returnList.add(appointment);
            }
        }
        return returnList;
    }
*/
}
