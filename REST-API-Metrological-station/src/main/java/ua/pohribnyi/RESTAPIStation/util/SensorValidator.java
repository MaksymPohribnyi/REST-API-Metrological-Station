package ua.pohribnyi.RESTAPIStation.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ua.pohribnyi.RESTAPIStation.models.Sensor;
import ua.pohribnyi.RESTAPIStation.service.SensorService;

@Component
public class SensorValidator implements Validator {

	private final SensorService sensorService;

	@Autowired
	public SensorValidator(SensorService sensorService) {
		this.sensorService = sensorService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Sensor.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Sensor sample = (Sensor) target;
		if (sensorService.findSensorByName(sample.getName()).isPresent())
			errors.rejectValue("Name", null, "Sensor with presented name has already exists!");
	}

}
