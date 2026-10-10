package com.travel.util;

public class GenerateHashMain {
    public static void main(String[] args) {
        String password = "Pass@123";
        String hash = PasswordUtil.hashPassword(password);
        System.out.println("\n--------------------------------------------------");
        System.out.println("Generated PBKDF2 Hash for '" + password + "':");
        System.out.println(hash);
        System.out.println("--------------------------------------------------");
        System.out.println("Copy the hash string above and paste it in seed.sql");
    }
}
