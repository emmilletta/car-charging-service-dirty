package com.emmilletta.ccs;

import com.emmilletta.ccs.dto.VendorEnum;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alla Danko
 */
@Entity
@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"sessions"})
@EqualsAndHashCode(exclude = {"sessions"})
@GenericGenerator(
        name = "vehicle_generator",
        strategy = "enhanced-sequence",
        parameters = {
                @Parameter(name = "optimizer", value = "pooled-lo"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1"),
                @Parameter(name = "sequence_name", value = "ccs_vehicle_sqc")
        })
@Table(name = "vehicle", schema = "ccs")
public class VehicleEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_generator")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "vehicle_id", nullable = false)
    private String vehicleId;

    @Column(name = "vendor", nullable = false)
    @Enumerated(EnumType.STRING)
    private VendorEnum vendor;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<SessionEntity> sessions = new ArrayList<>();
}
