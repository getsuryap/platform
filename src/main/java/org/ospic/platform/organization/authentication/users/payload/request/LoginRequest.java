package org.ospic.platform.organization.authentication.users.payload.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class LoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;
	private String tenantId;

}
