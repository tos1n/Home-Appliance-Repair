package com.example.ffive.seg2105ffiveproject;

public class Admin extends Account{

    private static boolean exists = false;

    Admin(){
        super("admin", "admin", "Administrator");
        exists = true;
    }

    public static boolean isExisting(){
        return exists;
    }
}
