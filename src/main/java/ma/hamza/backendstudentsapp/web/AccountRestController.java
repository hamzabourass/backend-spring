package ma.hamza.backendstudentsapp.web;

import ma.hamza.backendstudentsapp.dtos.RoleUserForm;
import ma.hamza.backendstudentsapp.entities.AppRole;
import ma.hamza.backendstudentsapp.entities.AppUser;
import ma.hamza.backendstudentsapp.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200") // Allow requests from this origin

public class AccountRestController {

    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path="/users")
    List<AppUser> listUsers(){
        return accountService.listUsers();
    }

    @PostMapping(path = "/users")
    AppUser saveUser(@RequestBody AppUser appUser){
        return accountService.addNewUser(appUser);
    }

    @GetMapping("/roles")
    List<AppRole> listRoles(){
        return accountService.listRoles();
    }

    @PostMapping(path = "/roles")
    AppRole saveRole(@RequestBody AppRole appRole){
        return accountService.addNewRole(appRole);
    }

    @PostMapping(path = "/addRoleToUser")
    void addRoleToUser(@RequestBody RoleUserForm roleUserForm){
        accountService.addRoleToUser(roleUserForm.getUsername(),roleUserForm.getRoleName());
    }
    @GetMapping("/profile")
    ResponseEntity<AppUser> getCurrentUser(){
        return accountService.authenticatedUser();
    }
}
