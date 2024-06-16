package ma.hamza.backendstudentsapp.dtos;

import lombok.Data;

@Data
public class TokenDTO {
    String accessToken;
    String refreshToken;
}
