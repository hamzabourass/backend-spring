package ma.hamza.backendstudentsapp.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.hamza.backendstudentsapp.dtos.LoginResponse;
import ma.hamza.backendstudentsapp.dtos.LoginUserDto;
import ma.hamza.backendstudentsapp.dtos.RegisterUserDto;
import ma.hamza.backendstudentsapp.dtos.TokenDTO;
import ma.hamza.backendstudentsapp.entities.AppUser;
import ma.hamza.backendstudentsapp.service.AccountService;
import ma.hamza.backendstudentsapp.service.AuthenticationService;
import ma.hamza.backendstudentsapp.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final AccountService accountService;


    @PostMapping("/signup")
    public ResponseEntity<AppUser> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        AppUser registeredUser = authenticationService.signup(registerUserDto);
        accountService.addRoleToUser(registeredUser.getEmail(),"USER");

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginUserDto loginUserDto) {
        AppUser authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);
        String refreshToken = jwtService.refreshToken(jwtToken);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(jwtToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
    @GetMapping("/refresh")
    public ResponseEntity<TokenDTO> refresh(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {
        return new ResponseEntity<>(authenticationService.refresh(refreshToken), HttpStatus.OK);
    }
}