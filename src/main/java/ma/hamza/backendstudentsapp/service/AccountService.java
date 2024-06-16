package ma.hamza.backendstudentsapp.service;

import ma.hamza.backendstudentsapp.entities.AppRole;
import ma.hamza.backendstudentsapp.entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser(String username, String roleName);
    AppUser loadUserByUsername(String username);
    List<AppUser> listUsers();
}
