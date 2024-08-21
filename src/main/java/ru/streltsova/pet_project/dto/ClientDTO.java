package ru.streltsova.pet_project.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ClientDTO {
    @NotEmpty(message = "Login not should be empty!")
    @Email(message = "Should be email format")
    private String login;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
