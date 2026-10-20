package be.thomasmore.nudibranch.repositories;

import be.thomasmore.nudibranch.model.Sighting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SightingRepository extends CrudRepository<Sighting, Integer> {
    @Query("""
                SELECT s
                FROM Sighting s
                WHERE (:divespot IS NULL OR :divespot = ''
                       OR LOWER(s.location.locationDivespot)
                          LIKE LOWER(CONCAT('%', :divespot, '%')))
                  AND (:country IS NULL OR :country = ''
                       OR LOWER(s.location.locationCountry)
                          LIKE LOWER(CONCAT('%', :country, '%')))
                  AND (:nudibranch IS NULL OR :nudibranch=''
                  OR LOWER(s.nudibranch.commonName)
                              LIKE LOWER(CONCAT('%',:nudibranch,'%')))
            """)
    List<Sighting> findByFilter(@Param("divespot") String divespot,
                                @Param("country") String country,
                                @Param("nudibranch") String nudibranch);

}
