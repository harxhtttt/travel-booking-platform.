package com.travel.model;

/** Shows Inheritance (extends User). */
public class Traveler extends User {
    public Traveler() { super(); }
    public Traveler(int id, String name, String email, String passwordHash) {
        super(id, name, email, passwordHash, Role.TRAVELER);
    }
}
