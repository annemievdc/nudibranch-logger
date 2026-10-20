package be.thomasmore.nudibranch.repositories;

import be.thomasmore.nudibranch.model.Nudibranch;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface NudiRepository extends CrudRepository<Nudibranch, Integer> {


    @Query("SELECT n FROM Nudibranch n " +
            "WHERE (:keyword IS NULL OR n.commonName ILIKE CONCAT('%', :keyword, '%') " +
            "     OR n.scientificName ILIKE CONCAT('%', :keyword, '%') " +
            "     OR n.description ILIKE CONCAT('%', :keyword, '%')) " +
            "AND (:commonName IS NULL OR n.commonName ILIKE CONCAT('%', :commonName, '%')) " +
            "AND (:scientificName IS NULL OR n.scientificName ILIKE CONCAT('%', :scientificName, '%')) " +
            "AND (:venomous IS NULL OR n.venomous = :venomous)" +
            "ORDER BY n.commonName ASC")

    List<Nudibranch> findByFilters(@Param("keyword") String keyword,
                                   @Param("commonName") String commonName,
                                   @Param("scientificName") String scientificName,
                                   @Param("venomous") Boolean venomous);


}



