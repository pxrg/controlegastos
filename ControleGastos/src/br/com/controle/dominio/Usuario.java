package br.com.controle.dominio;

/**
 * @author igor.santos
 */
public class Usuario {

    public static final String TABLE = "USUARIO";
    public static final String ID = "ID";
    private int id;
    public static final String PASSWORD = "PASSWORD";
    private String password;
    public static final String EMAIL = "EMAIL";
    private String email;

    public Usuario(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
