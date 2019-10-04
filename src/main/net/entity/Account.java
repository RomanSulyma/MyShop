package net.entity;

import net.model.CurrentAccount;

public class Account extends AbstractEntity<Integer> implements CurrentAccount {
    private static final long serialVersionUID = -3196229925974576545L;
    private String name;
    private String email;
    //private String avatarUrl;

    public Account() {
        super();
    }

    public Account(String name, String email) {
        super();
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }*/

    @Override
    public String toString() {
        return String.format("Account [id=%s, name=%s, email=%s]", getId(), name, email);
    }
    //get user info
    @Override
    public String getDescription() {
        return name + "(" + email + ")";
    }
}
