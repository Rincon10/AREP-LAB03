package edu.escuelaing.arep.dto;

/**
 * @author Iván Camilo Rincón Saavedra
 * @version 1.0 4/5/2022
 * @project SecureApp
 */
public class UserDto {
    private String email;
    private String password;

    public UserDto() {
    }

    public UserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
