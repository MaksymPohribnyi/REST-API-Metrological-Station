package ua.pohribnyi.RESTAPIStation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.pohribnyi.RESTAPIStation.models.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

	Optional<Sensor> findByName(String name);

}
