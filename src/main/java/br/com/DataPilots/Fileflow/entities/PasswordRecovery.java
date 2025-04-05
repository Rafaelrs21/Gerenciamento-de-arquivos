package br.com.DataPilots.Fileflow.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name="password_recoveries")
@Entity(name="PasswordRecovery")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class PasswordRecovery {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String token;
    @ManyToOne @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;
    private LocalDateTime expiresAt;
    @Setter
    private boolean isUsed;

    public PasswordRecovery(String token, User user, LocalDateTime expiresAt) {
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
        isUsed = false;
    }

    public boolean isExpired() {
        return isUsed || LocalDateTime.now().isAfter(expiresAt);
    }
}
