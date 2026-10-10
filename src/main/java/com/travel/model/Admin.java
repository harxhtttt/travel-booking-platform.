package com.travel.model;

/** Shows Inheritance (extends User). */
public class Admin extends User {
    public Admin() { super(); }
    public Admin(int id, String name, String email, String passwordHash) {
        super(id, name, email, passwordHash, Role.ADMIN);
    }
}
