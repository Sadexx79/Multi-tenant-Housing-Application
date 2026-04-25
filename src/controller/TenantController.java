package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Tenant;

public class TenantController {

    private ObservableList<Tenant> tenants = FXCollections.observableArrayList();

    public ObservableList<Tenant> getTenants() {
        return tenants;
    }

    public void addTenant(Tenant tenant) {
        tenants.add(tenant);
    }

    public void removeTenant(Tenant tenant) {
        tenants.remove(tenant);
    }

    public void updateTenant(Tenant tenant, String name, double rent, double utilities, int leaseMonths) {
        tenant.setName(name);
        tenant.setRent(rent);
        tenant.setUtilities(utilities);
        tenant.setLeaseMonths(leaseMonths);
    }
}