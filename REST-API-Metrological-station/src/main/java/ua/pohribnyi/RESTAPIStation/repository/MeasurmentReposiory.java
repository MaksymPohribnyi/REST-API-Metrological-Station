package ua.pohribnyi.RESTAPIStation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.pohribnyi.RESTAPIStation.models.Measurement;

@Repository
public interface MeasurmentReposiory extends JpaRepository<Measurement, Integer> {

	long countByItsRaining(boolean itsRaining);

}
