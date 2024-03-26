package ua.pohribnyi.RESTAPIStation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.pohribnyi.RESTAPIStation.models.Sensor;
import ua.pohribnyi.RESTAPIStation.repository.SensorRepository;

@Service
@Transactional(readOnly = true)
public class SensorService {

	private final SensorRepository sensorRepository;

	@Autowired
	public SensorService(SensorRepository sensorRepository) {
		this.sensorRepository = sensorRepository;
	}

	public List<Sensor> findAllSensors() {
		return sensorRepository.findAll();
	}

	public Optional<Sensor> findSensorByName(String name) {
		return sensorRepository.findByName(name);
	}

	@Transactional
	public void saveSensor(Sensor sensor) {
		sensorRepository.save(sensor);
	}

}
