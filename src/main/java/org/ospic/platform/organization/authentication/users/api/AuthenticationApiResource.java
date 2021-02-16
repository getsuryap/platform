package org.ospic.platform.organization.authentication.users.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.ospic.platform.organization.authentication.roles.services.RoleReadPrincipleServices;
import org.ospic.platform.organization.authentication.roles.services.RoleWritePrincipleService;
import org.ospic.platform.organization.authentication.users.payload.request.*;
import org.ospic.platform.organization.authentication.users.services.UsersReadPrincipleService;
import org.ospic.platform.organization.authentication.users.services.UsersWritePrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@ApiIgnore
@RequestMapping("/api/auth")
@Api(value = "/api/auth", tags = "Authentication")
public class AuthenticationApiResource {
    @Autowired
    UsersReadPrincipleService usersReadPrincipleService;
    @Autowired
    UsersWritePrincipleService usersWritePrincipleService;
    @Autowired
    RoleReadPrincipleServices roleReadPrincipleServices;
    @Autowired
    RoleWritePrincipleService roleWriteService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        return this.usersReadPrincipleService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN', 'SUPER_USER')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return this.usersWritePrincipleService.registerUser(signUpRequest);
    }

    @ApiOperation(value = "UPDATE User by ID", notes = "UPDATE User by ID")
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateUserDetails(@PathVariable("userId") Long userId, @RequestBody UpdateUserPayload payload) {
        return this.usersWritePrincipleService.updateUserDetails(userId, payload);
    }

    @ApiOperation(value = "RETRIEVE List of all Application Users", notes = "RETRIEVE List of all Application Users")
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveAllApplicationUsersResponse() {
        return this.usersReadPrincipleService.retrieveAllApplicationUsersResponse();
    }

    @ApiOperation(value = "RETRIEVE Logged in user", notes = "RETRIEVE logged in user")
    @RequestMapping(value = "/users/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    ResponseEntity<?> retrieveLoggerInUser() {
        return this.usersReadPrincipleService.retrieveLoggerInUser();
 }

    @ApiOperation(value = "RETRIEVE User by ID", notes = "RETRIEVE User by ID")
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveUserById(@PathVariable("userId") Long userId) {
       return this.usersReadPrincipleService.retrieveUserById(userId);
    }

    @ApiOperation(value = "LOGOUT Session", notes = "LOGOUT Session")
    @RequestMapping(value = "/signout", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    public ResponseEntity<?> logoutSession(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
      return this.usersWritePrincipleService.logoutSession(httpServletRequest, httpServletResponse);
    }

    @ApiOperation(value = "Update user password", notes = "Update user password")
    @RequestMapping(value = "/password", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(@ApiResponse(code = 200, message = "", response = UserRequestDataApiResourceSwagger.GetUserRequestDataResponse.class))
    public ResponseEntity<?> updateUserPassword(@Valid @RequestBody PasswordUpdatePayload payload) {
        return this.usersWritePrincipleService.updateUserPassword(payload);
    }

    @ApiOperation(value = "RETRIEVE all roles", notes = "RETRIEVE all roles")
    @RequestMapping(value = "/roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('MODERATOR')")
    ResponseEntity<?> retrieveAllRoles() {
        return roleReadPrincipleServices.retrieveAllRoles();
    }

    @ApiOperation(value = "RETRIEVE role by ID", notes = "RETRIEVE role by ID")
    @RequestMapping(value = "/roles/{roleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveRoleById(@PathVariable Long roleId) {
        return roleReadPrincipleServices.fetchRoleById(roleId);
    }

    @ApiOperation(value = "UPDATE role privilege", notes = "UPDATE role privilege")
    @RequestMapping(value = "/roles/{roleId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateRoleById(@PathVariable Long roleId, @RequestBody List<Long> privileges) {
        return roleWriteService.updateRole(roleId, privileges);
    }

    @ApiOperation(value = "RETRIEVE all authorities", notes = "RETRIEVE all authorities")
    @RequestMapping(value = "/authorities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> fetchAllAvailableAuthorities() {
        return roleReadPrincipleServices.fetchAuthorities();
    }

    @RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
        return this.usersWritePrincipleService.refreshToken(request);
    }

}
