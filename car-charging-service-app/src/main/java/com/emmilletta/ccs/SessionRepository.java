package com.emmilletta.ccs;

import com.emmilletta.ccs.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Alla Danko
 */
@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    @Query("select se from SessionEntity se join se.user ue where ue.userId = :userId and "
            + "se.sessionId = :sessionId and ue.status = 'ACTIVE'")
    Optional<SessionEntity> findActiveSessionByUserId(@Param("userId") String userId,
                                                      @Param("sessionId") String sessionId);
}
