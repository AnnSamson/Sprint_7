public class Courier {
    private String login;
    private String passwordHash;
    private String firstName;

    public Courier(String login, String passwordHash, String firstName) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
    }

    public Courier() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String toJson() {
        return String.format("{\"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"%s\"}", login, passwordHash, firstName);
    }
}
