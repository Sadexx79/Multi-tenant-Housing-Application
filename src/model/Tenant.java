package model;

import javafx.beans.property.*;

public class Tenant {
    private StringProperty name;
    private DoubleProperty rent;
    private DoubleProperty utilities;
    private IntegerProperty leaseMonths;

    public Tenant(String name, double rent, double utilities, int leaseMonths) {
        this.name = new SimpleStringProperty(name);
        this.rent = new SimpleDoubleProperty(rent);
        this.utilities = new SimpleDoubleProperty(utilities);
        this.leaseMonths = new SimpleIntegerProperty(leaseMonths);
    }

    public String getName() { return name.get(); }
    public double getRent() { return rent.get(); }
    public double getUtilities() { return utilities.get(); }
    public int getLeaseMonths() { return leaseMonths.get(); }

    public void setName(String value) { name.set(value); }
    public void setRent(double value) { rent.set(value); }
    public void setUtilities(double value) { utilities.set(value); }
    public void setLeaseMonths(int value) { leaseMonths.set(value); }

    public StringProperty nameProperty() { return name; }
    public DoubleProperty rentProperty() { return rent; }
    public DoubleProperty utilitiesProperty() { return utilities; }
    public IntegerProperty leaseMonthsProperty() { return leaseMonths; }
}
