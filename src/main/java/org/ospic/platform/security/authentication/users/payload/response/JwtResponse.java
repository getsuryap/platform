package org.ospic.platform.security.authentication.users.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class JwtResponse {
    private String accessToken;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private List<String> permissions;

    public JwtResponse loginResponse(String accessToken, Long id, String username,
                                     String email, List<String> roles, List<String> permissions) {
        return new JwtResponse(accessToken, id, username, email, roles, permissions);
    }

   private JwtResponse(
            String accessToken, Long id, String username,
            String email, List<String> roles, List<String> permissions) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.permissions = permissions;
        this.accessToken = accessToken;
    }

}
