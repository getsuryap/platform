package org.ospic.authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.ospic.authentication.exceptions.UserAuthenticationException;
import org.ospic.authentication.payload.request.UserRequestData;
import org.ospic.authentication.payload.request.UserRequestDataApiResourceSwagger;
import org.ospic.authentication.users.User;
import org.ospic.authentication.users.repository.UserRepository;
import org.ospic.domain.CustomReponseMessage;
import org.ospic.util.enums.RoleEnums;
import org.ospic.authentication.roles.Role;
import org.ospic.authentication.payload.request.LoginRequest;
import org.ospic.authentication.payload.request.SignupRequest;
import org.ospic.authentication.payload.response.JwtResponse;
import org.ospic.authentication.payload.response.MessageResponse;
import org.ospic.authentication.roles.repository.RoleRepository;
import org.ospic.security.jwt.JwtUtils;
import org.ospic.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));


        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        logger.info(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()));
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        List<Role> roles = new ArrayList<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleEnums.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleEnums.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(RoleEnums.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleEnums.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
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

    @ApiOperation(value = "RETRIEVE User by ID", notes = "RETRIEVE User by ID")
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> retrieveAllUserById(@PathVariable("userId") Long userId) {
        Optional<User> optional = userRepository.findById(userId);
        return ResponseEntity.badRequest().body(optional.isPresent() ? optional.get() : new CustomReponseMessage(String.format("User with ID %2d is not found", userId)));

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
        return userRepository.findById(u.getUserId()).map(user -> {
            String userPassword = user.getPassword();
            if (!(encoder.matches(u.getOldPassword(), userPassword))) {
                cm.setMessage("Invalid old Password");
                return new ResponseEntity<CustomReponseMessage>(cm, httpHeaders, HttpStatus.BAD_REQUEST);
            }
            user.setPassword(encoder.encode(u.getNewPassword()));
            userRepository.save(user);
            cm.setMessage("Password Updated Successfully ...");
            return new ResponseEntity<CustomReponseMessage>(cm, httpHeaders, HttpStatus.OK);


        }).orElseThrow(() -> new UserAuthenticationException(u.getUserId()));
    }


}
