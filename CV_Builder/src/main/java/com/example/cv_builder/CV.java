
package com.example.cv_builder;

public class CV {
    private final int id;
    private final String name;
    private final String email;

    public CV(int id, String name, String email) {
        this.id = id;
        this.name = name == null ? "" : name;
        this.email = email == null ? "" : email;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        String nm = (name == null || name.isBlank()) ? "<no name>" : name;
        return id + " - " + nm;
    }
}
