package com.travel.model;

/** Shows Inheritance (extends User). */
public class TravelAgent extends User {
    public TravelAgent() { super(); }
    public TravelAgent(int id, String name, String email, String passwordHash) {
        super(id, name, email, passwordHash, Role.AGENT);
    }
}
