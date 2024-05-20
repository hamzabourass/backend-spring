package ma.hamza.backendstudentsapp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString @Builder
public class Student {

    @Id()
    private String id;
    @Column(unique = true)
    private String code;
    private String firstName;
    private String lastName;
    private String programId;
    private String email;
    private String photo;

}
