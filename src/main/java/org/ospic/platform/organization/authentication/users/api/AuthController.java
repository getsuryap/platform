package org.ospic.platform.organization.authentication.users.api;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


import io.jsonwebtoken.impl.DefaultClaims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.ospic.platform.organization.authentication.users.services.UsersReadPrincipleService;
import org.ospic.platform.organization.authentication.users.services.UsersWritePrincipleService;
import org.ospic.platform.organization.departments.repository.DepartmentJpaRepository;
import org.ospic.platform.organization.staffs.service.StaffsWritePrinciplesService;
import org.ospic.platform.organization.authentication.roles.services.RoleReadPrincipleServices;
import org.ospic.platform.organization.authentication.roles.services.RoleWritePrincipleService;
import org.ospic.platform.organization.authentication.users.data.RefreshTokenResponse;
import org.ospic.platform.organization.authentication.users.exceptions.UserAuthenticationExceptionPlatform;
import org.ospic.platform.organization.authentication.users.payload.request.UserRequestData;
import org.ospic.platform.organization.authentication.users.payload.request.UserRequestDataApiResourceSwagger;
import org.ospic.platform.organization.authentication.users.domain.User;
import org.ospic.platform.organization.authentication.users.repository.UserRepository;
import org.ospic.platform.domain.CustomReponseMessage;
import org.ospic.platform.organization.authentication.roles.domain.Role;
import org.ospic.platform.organization.authentication.users.payload.request.LoginRequest;
import org.ospic.platform.organization.authentication.users.payload.request.SignupRequest;
import org.ospic.platform.organization.authentication.users.payload.response.JwtResponse;
import org.ospic.platform.organization.authentication.users.payload.response.MessageResponse;
import org.ospic.platform.organization.authentication.roles.repository.RoleRepository;
import org.ospic.platform.security.jwt.JwtUtils;
import org.ospic.platform.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@ApiIgnore
@RequestMapping("/api/auth")
/**Defining class-level request handling**/
@Api(value = "/api/auth", tags = "Authentication")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UsersReadPrincipleService usersReadPrincipleService;
    @Autowired
    UsersWritePrincipleService usersWritePrincipleService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    StaffsWritePrinciplesService staffsWritePrinciplesService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RoleReadPrincipleServices roleReadPrincipleServices;
    @Autowired
    RoleWritePrincipleService roleWriteService;
    @Autowired
    DepartmentJpaRepository departmentJpaRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        return this.usersReadPrincipleService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return this.usersWritePrincipleService.registerUser(signUpRequest);
    }

    @ApiOperation(value = "RETRIEVE List of all Application Users", notes = "RETRIEVE List of all Application Users")
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<User>> retrieveAllApplicationUsersResponse() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            user.setPassword(null);
        });
        return ResponseEntity.ok(users);
    }

    @ApiOperation(value = "RETRIEVE Logged in user", notes = "RETRIEVE logged in user")
    @RequestMapping(value = "/users/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveLoggerInUser() {
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optional = userRepository.findById(ud.getId());
        return ResponseEntity.ok().body(optional.isPresent() ? optional.get() : new CustomReponseMessage(HttpStatus.NOT_FOUND.value(), String.format("User with ID %2d is not found")));
    }

    @ApiOperation(value = "RETRIEVE User by ID", notes = "RETRIEVE User by ID")
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveUserById(@PathVariable("userId") Long userId) {
        Optional<User> optional = userRepository.findById(userId);
        return ResponseEntity.ok().body(optional.isPresent() ? optional.get() : new CustomReponseMessage(HttpStatus.NOT_FOUND.value(), String.format("User with ID %2d is not found", userId)));

    }

    @ApiOperation(value = "LOGOUT Session", notes = "LOGOUT Session")
    @RequestMapping(value = "/signout", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public String logoutSession(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.setInvalidateHttpSession(true);
            logoutHandler.logout(httpServletRequest, httpServletResponse, authentication);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/";
    }

    @ApiOperation(value = "Update user password", notes = "Update user password")
    @RequestMapping(value = "/password", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(@ApiResponse(code = 200, message = "", response = UserRequestDataApiResourceSwagger.GetUserRequestDataResponse.class))
    @ResponseBody
    public ResponseEntity<?> updateUserPassword(@Valid @RequestBody UserRequestData u) {
        CustomReponseMessage cm = new CustomReponseMessage();
        HttpHeaders httpHeaders = new HttpHeaders();
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(ud.getId()).map(user -> {
            String userPassword = user.getPassword();
            if (!(encoder.matches(u.getOldPassword(), userPassword))) {
                cm.setMessage("Invalid old Password");
                cm.setHttpStatus(HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<CustomReponseMessage>(cm, httpHeaders, HttpStatus.BAD_REQUEST);
            }
            user.setPassword(encoder.encode(u.getNewPassword()));
            userRepository.save(user);
            cm.setMessage("Password Updated Successfully ...");
            cm.setHttpStatus(HttpStatus.OK.value());
            return new ResponseEntity<CustomReponseMessage>(cm, httpHeaders, HttpStatus.OK);


        }).orElseThrow(() -> new UserAuthenticationExceptionPlatform(ud.getId()));
    }

    @ApiOperation(value = "RETRIEVE all roles", notes = "RETRIEVE all roles")
    @RequestMapping(value = "/roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    @ResponseBody
    ResponseEntity<?> retrieveAllRoles() {
        return roleReadPrincipleServices.retrieveAllRoles();
    }

    @ApiOperation(value = "RETRIEVE role by ID", notes = "RETRIEVE role by ID")
    @RequestMapping(value = "/roles/{roleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveRoleById(@PathVariable Long roleId) {
        return roleReadPrincipleServices.fetchRoleById(roleId);
    }

    @ApiOperation(value = "UPDATE role privilege", notes = "UPDATE role privilege")
    @RequestMapping(value = "/roles/{roleId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> updateRoleById(@PathVariable Long roleId, @RequestBody List<Long> privileges) {
        return roleWriteService.updateRole(roleId, privileges);
    }

    @ApiOperation(value = "RETRIEVE all authorities", notes = "RETRIEVE all authorities")
    @RequestMapping(value = "/authorities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> fetchAllAvailableAuthorities() {
        return roleReadPrincipleServices.fetchAuthorities();
    }

    @RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
        // From the HttpRequest get the claims
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

        Map<String, Object> expectedMap = getMapFromIoJsonWebTokenClaims(claims);
        String token = jwtUtils.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        return ResponseEntity.ok(new RefreshTokenResponse(token));
    }

    public Map<String, Object> getMapFromIoJsonWebTokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

}
