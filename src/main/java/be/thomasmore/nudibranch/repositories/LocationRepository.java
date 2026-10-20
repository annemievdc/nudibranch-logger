package be.thomasmore.nudibranch.repositories;

import org.springframework.data.repository.CrudRepository;
import be.thomasmore.nudibranch.model.Location;

public interface LocationRepository extends CrudRepository<Location, Integer> {

}
