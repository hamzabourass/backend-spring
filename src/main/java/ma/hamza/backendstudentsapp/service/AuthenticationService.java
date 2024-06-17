package ma.hamza.backendstudentsapp.service;

import lombok.AllArgsConstructor;
import ma.hamza.backendstudentsapp.dtos.LoginUserDto;
import ma.hamza.backendstudentsapp.dtos.RegisterUserDto;
import ma.hamza.backendstudentsapp.dtos.TokenDTO;
import ma.hamza.backendstudentsapp.email.EmailService;
import ma.hamza.backendstudentsapp.entities.AppUser;
import ma.hamza.backendstudentsapp.repository.AppUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class AuthenticationService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;


    public AppUser signup(RegisterUserDto input) {
        AppUser user = new AppUser();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    public AppUser authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
    public TokenDTO refresh(String refreshToken) {
        refreshToken = refreshToken.substring(7);
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setRefreshToken(refreshToken);
        tokenDTO.setAccessToken(jwtService.refreshToken(refreshToken));
        return tokenDTO;
    }

    private void sendValidationEmail(RegisterUserDto registerUserDto) {
    }
}
