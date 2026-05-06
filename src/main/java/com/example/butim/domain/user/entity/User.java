package com.example.butim.domain.user.entity;

import com.example.butim.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean termsAgreed;

    @Column(nullable = false)
    private boolean pushAlarmAgreed;

    public void update(String name, String email, String password, String phoneNumber, boolean termsAgreed, boolean pushAlarmAgreed) {
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (password != null) this.password = password;
        if (phoneNumber != null) this.phoneNumber = phoneNumber;
        this.termsAgreed = termsAgreed;
        this.pushAlarmAgreed = pushAlarmAgreed;
    }

    public void withdraw() {
        softDelete();
    }
}