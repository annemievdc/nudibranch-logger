package be.thomasmore.nudibranch.repositories;

import be.thomasmore.nudibranch.model.Diet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DietRepository extends CrudRepository<Diet, Integer> {
@Query("SELECT d FROM Diet d " +
        "WHERE (:dietCommonName IS NULL OR d.dietCommonName ILIKE CONCAT ('%', :dietCommonName, '%'))" +
        "AND (:dietScientificName IS NULL OR d.dietScientificName ILIKE CONCAT('%', :dietScientificName, '%')) ")
    List<Diet> findByDietFilter(@Param("dietCommonName") String dietCommonName,
                            @Param("dietScientificName") String dietScientificName);
}
