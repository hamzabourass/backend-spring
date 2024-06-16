package ma.hamza.backendstudentsapp.dtos;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;

    private String refreshToken;

    private long expiresIn;

}