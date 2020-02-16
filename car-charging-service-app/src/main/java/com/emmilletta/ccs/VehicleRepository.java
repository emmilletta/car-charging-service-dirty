package com.emmilletta.ccs;

import com.emmilletta.ccs.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alla Danko
 */
@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
}
