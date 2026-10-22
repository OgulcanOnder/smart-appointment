package com.ogulcanonder.smart_appointment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @OneToOne
    private Account account;

    public Employee() {
    }

    public Employee(long id, String name, String surname, Business business, Account account) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.business = business;
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Employee(Builder builder) {
        this.name = builder.name;
        this.surname = builder.surname;
        this.business = builder.business;
        this.account = builder.account;
    }

    public static class Builder {
        private String name;
        private String surname;
        private Business business;
        private Account account;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder business(Business business) {
            this.business = business;
            return this;
        }

        public Builder account(Account account) {
            this.account = account;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }

}
