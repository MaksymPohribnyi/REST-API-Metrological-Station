package ua.pohribnyi.RESTAPIStation.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.pohribnyi.RESTAPIStation.models.Measurement;
import ua.pohribnyi.RESTAPIStation.models.Sensor;
import ua.pohribnyi.RESTAPIStation.repository.MeasurmentReposiory;

@Service
@Transactional(readOnly = true)
public class MeasurmentService {

	private final MeasurmentReposiory measurmentReposiory;
	private final SensorService sensorService;

	@Autowired
	public MeasurmentService(MeasurmentReposiory measurmentReposiory, SensorService sensorService) {
		this.measurmentReposiory = measurmentReposiory;
		this.sensorService = sensorService;
	}

	public List<Measurement> findAllMeasurments() {
		return measurmentReposiory.findAll();
	}

	public List<Measurement> findAllMeasurmentsPageable(int numbOfPage, int measurPerPage) {
		return measurmentReposiory.findAll(PageRequest.of(numbOfPage, measurPerPage)).getContent();
	}

	@Transactional
	public void saveMeasurment(Measurement measurment) {
		Sensor sensorName = measurment.getSensor();
		Sensor sensorEntity = sensorService.findSensorByName(sensorName.getName()).get();
		measurment.setSensor(sensorEntity);
		enrichMeasurment(measurment);
		measurmentReposiory.save(measurment);
	}

	private void enrichMeasurment(Measurement measurment) {
		measurment.setCreatedAt(LocalDateTime.now());
		measurment.setUpdatedAt(LocalDateTime.now());
	}

	public long countRainyDays() {
		return measurmentReposiory.countByItsRaining(true);
	}

}
