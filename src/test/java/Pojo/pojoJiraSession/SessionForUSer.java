package Pojo.pojoJiraSession;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SessionForUSer {

    private String username;
    private String password;

    public SessionForUSer(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
