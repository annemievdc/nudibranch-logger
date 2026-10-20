package be.thomasmore.nudibranch.repositories;

import be.thomasmore.nudibranch.model.Friend;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface FriendRepository extends CrudRepository<Friend, Integer> {

    @Query("""
            SELECT f FROM Friend f
            WHERE (:friendName IS NULL OR f.friendName ILIKE CONCAT('%', :friendName, '%'))
              AND (:friendEmail IS NULL OR f.friendEmail ILIKE CONCAT('%', :friendEmail, '%'))
                          ORDER BY f.friendName ASC""")
    List<Friend> findByFilters(
            @Param("friendName") String friendName,
            @Param("friendEmail") String friendEmail


    );

    @Query("""
SELECT f FROM Friend f WHERE (:friendUsername IS NULL OR f.friendUsername = :friendUsername)
""")
    Optional<Friend> findByFriendUsername(@Param("friendUsername") String friendUsername);


}
