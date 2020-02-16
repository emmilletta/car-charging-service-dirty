package com.emmilletta.ccs;

import com.emmilletta.ccs.UserEntity;
import com.emmilletta.ccs.dto.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Alla Danko
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select ue from UserEntity ue where ue.status = 'ACTIVE' and ue.userId = :userId")
    Optional<UserEntity> findByActiveUserId(@Param("userId") String userId);

    @Query("select ue from UserEntity ue where ue.userId = :userId")
    Optional<UserEntity> findByUserId(@Param("userId") String userId);

    @Query("select ue from UserEntity ue where ue.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);

    @Query("select ue from UserEntity ue where (:status is null or ue.status = :status) " +
            "and (:startDate is null or ue.registrationDate >= :startDate) " +
            "and (:stopDate is null or ue.registrationDate <= :stopDate)")
    List<UserEntity> findByStatusAndDates(@Param("status") UserStatus status,
                                          @Param("startDate") ZonedDateTime startDate,
                                          @Param("stopDate") ZonedDateTime stopDate);
}
