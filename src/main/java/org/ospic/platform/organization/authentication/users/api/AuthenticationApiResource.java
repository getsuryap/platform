package org.ospic.platform.organization.authentication.users.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.ospic.platform.fileuploads.data.EntityType;
import org.ospic.platform.fileuploads.message.ResponseMessage;
import org.ospic.platform.fileuploads.service.FilesStorageService;
import org.ospic.platform.organization.authentication.roles.data.RoleRequest;
import org.ospic.platform.organization.authentication.roles.services.RoleReadPrincipleServices;
import org.ospic.platform.organization.authentication.roles.services.RoleWritePrincipleService;
import org.ospic.platform.organization.authentication.selfservice.data.SelfServicePayload;
import org.ospic.platform.organization.authentication.users.domain.User;
import org.ospic.platform.organization.authentication.users.payload.request.*;
import org.ospic.platform.organization.authentication.users.services.UsersReadPrincipleService;
import org.ospic.platform.organization.authentication.users.services.UsersWritePrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@ApiIgnore
@RequestMapping("/api/auth")
@Api(value = "/api/auth", tags = "Authentication")
public class AuthenticationApiResource {
   private final UsersReadPrincipleService usersReadPrincipleService;
   private final UsersWritePrincipleService usersWritePrincipleService;
   private final RoleReadPrincipleServices roleReadPrincipleServices;
   private final RoleWritePrincipleService roleWriteService;
   private final FilesStorageService filesStorageService;

    @Autowired
    AuthenticationApiResource(
            UsersReadPrincipleService usersReadPrincipleService,
                    UsersWritePrincipleService usersWritePrincipleService,
                    RoleReadPrincipleServices roleReadPrincipleServices,
                    RoleWritePrincipleService roleWriteService,
            FilesStorageService filesStorageService){
        this.roleWriteService = roleWriteService;
        this.roleReadPrincipleServices = roleReadPrincipleServices;
        this.usersWritePrincipleService = usersWritePrincipleService;
        this.usersReadPrincipleService = usersReadPrincipleService;
        this.filesStorageService = filesStorageService;

    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        return this.usersReadPrincipleService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'CREATE_USER')")
    public ResponseEntity<?> registerSelfServiceUser(@Valid @RequestBody SignupRequest payload) {
        return this.usersWritePrincipleService.registerUser(payload);
    }

    @PostMapping("/self")
    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'CREATE_SELF_SERVICE','UPDATE_SELF_SERVICE')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SelfServicePayload payload) {
        return this.usersWritePrincipleService.registerSelfServiceUser(payload);
    }

    @ApiOperation(value = "UPDATE User by ID", notes = "UPDATE User by ID")
    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'UPDATE_USER')")
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateUserDetails(@PathVariable("userId") Long userId, @RequestBody UpdateUserPayload payload) {
        return this.usersWritePrincipleService.updateUserDetails(userId, payload);
    }


    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'READ_USER')")
    @ApiOperation(value = "RETRIEVE List of all Application Users", notes = "RETRIEVE List of all Application Users")
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveAllApplicationUsersResponse(@RequestParam(value = "command", required = false) String command) {
        if (!(command == null || command.isEmpty())) {
            if (command.equals("self")) {
                return this.usersReadPrincipleService.retrieveAllSelfServiceUsersResponse();
            }
            if (command.equals("users")) {
                return this.usersReadPrincipleService.retrieveAllUsersWhoAreNotSelfService();
            }
        }
        return this.usersReadPrincipleService.retrieveAllApplicationUsersResponse();
    }

    @ApiOperation(value = "RETRIEVE Logged in user", notes = "RETRIEVE logged in user")
    @RequestMapping(value = "/users/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    ResponseEntity<?> retrieveLoggerInUser() {
        return this.usersReadPrincipleService.retrieveLoggerInUser();
 }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'READ_USER')")
    @ApiOperation(value = "RETRIEVE User by ID", notes = "RETRIEVE User by ID")
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> retrieveUserById(@PathVariable("userId") Long userId) {
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

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'UPDATE_ROLE','CREATE_ROLE')")
    @ApiOperation(value = "CREATE new role", notes = "CREATE new role")
    @RequestMapping(value = "/roles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createNewRole(@Valid @RequestBody RoleRequest payload) {
        return roleWriteService.createNewRole(payload);
    }


    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'UPDATE_ROLE')")
    @ApiOperation(value = "RETRIEVE all roles", notes = "RETRIEVE all roles")
    @RequestMapping(value = "/roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveAllRoles() {
        return roleReadPrincipleServices.retrieveAllRoles();
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'READ_ROLE')")
    @ApiOperation(value = "RETRIEVE role by ID", notes = "RETRIEVE role by ID")
    @RequestMapping(value = "/roles/{roleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveRoleById(@PathVariable Long roleId) {
        return roleReadPrincipleServices.fetchRoleById(roleId);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'UPDATE_ROLE')")
    @ApiOperation(value = "UPDATE role privilege", notes = "UPDATE role privilege")
    @RequestMapping(value = "/roles/{roleId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateRoleById(@PathVariable Long roleId, @RequestBody RoleRequest payload) {
        return roleWriteService.updateRole(roleId, payload);
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'READ_ROLE')")
    @ApiOperation(value = "RETRIEVE all authorities", notes = "RETRIEVE all authorities")
    @RequestMapping(value = "/authorities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> fetchAllAvailableAuthorities() {
        return roleReadPrincipleServices.fetchAuthorities();
    }


    @RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) throws Exception {
        return this.usersWritePrincipleService.refreshToken(request);
    }

    @DeleteMapping("/self/{userId}")
    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS', 'CREATE_SELF_SERVICE','UPDATE_SELF_SERVICE','DELETE_SELF_SERVICE')")
    public ResponseEntity<?> deleteSelfServiceUser(@PathVariable(name = "userId", required = true) Long userId) {
        return this.usersWritePrincipleService.deleteSelfServiceUser(userId);
    }

    @ApiOperation(value = "UPDATE user thumbnail image", notes = "UPDATE user thumbnail image", response = User.class)
    @RequestMapping(value = "/{userId}/images", method = RequestMethod.PATCH, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUserProfileImage(@RequestParam("file") MultipartFile file, @PathVariable(name = "userId") Long userId) {
        String message = "";
        try {
            return this.usersWritePrincipleService.updateProfileImage(userId, file);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/{userId}/images/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename, @PathVariable Long userId) {
        Resource file = filesStorageService.loadImage(userId, EntityType.ENTITY_USER, filename,"images");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
