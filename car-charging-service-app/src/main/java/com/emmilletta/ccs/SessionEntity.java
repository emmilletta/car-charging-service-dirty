package com.emmilletta.ccs;

import com.emmilletta.ccs.dto.ProviderEnum;
import com.emmilletta.ccs.dto.SessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Currency;

/**
 * @author Alla Danko
 */
@Entity
@ToString
@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@GenericGenerator(
        name = "session_generator",
        strategy = "enhanced-sequence",
        parameters = {
                @Parameter(name = "optimizer", value = "pooled-lo"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1"),
                @Parameter(name = "sequence_name", value = "ccs_session_sqc")
        })
@Table(name = "session", schema = "ccs")
public class SessionEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_generator")
    private long id;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderEnum provider;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "stop_time")
    private ZonedDateTime stopTime;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "currency")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private VehicleEntity vehicle;
}
