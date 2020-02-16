package com.emmilletta.ccs;

import com.emmilletta.ccs.dto.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alla Danko
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString(exclude = {"vehicles", "sessions"})
@EqualsAndHashCode(exclude = {"vehicles", "sessions"})
@GenericGenerator(
        name = "user_generator",
        strategy = "enhanced-sequence",
        parameters = {
                @Parameter(name = "optimizer", value = "pooled-lo"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1"),
                @Parameter(name = "sequence_name", value = "ccs_user_sqc")
        })
@Table(name = "user", schema = "ccs")
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    private long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "provider_user_id")
    private String providerUserId;

    @Column(name = "user_access_key")
    private String userAccessKey;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "registration_date", nullable = false)
    private ZonedDateTime registrationDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<VehicleEntity> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SessionEntity> sessions = new ArrayList<>();
}
