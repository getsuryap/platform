package org.ospic.platform.organization.authentication.users.payload.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import javax.validation.constraints.*;
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
 
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    private Set<Long> roles;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    private Long departmentId;
    private Boolean isStaff;
}
