package com.easyschedule;

import com.people.Contact;

import java.sql.Date;

public class Appointment {
    private int appointmentId, userId, customerId;
    private String title, description, location, type;
    private Contact contact;
    private Date startDate, endDate;

    Appointment(int appointmentId, int userId, int customerId,
                String title, String description, String location,
                String type, Contact contact, Date startDate, Date endDate) {
        setAppointmentId(appointmentId);
        setUserId(userId);
        setCustomerId(customerId);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setType(type);
        setContact(contact);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}