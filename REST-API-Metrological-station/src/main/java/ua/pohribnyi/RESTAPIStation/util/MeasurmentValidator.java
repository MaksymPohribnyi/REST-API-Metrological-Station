package ua.pohribnyi.RESTAPIStation.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ua.pohribnyi.RESTAPIStation.models.Measurement;
import ua.pohribnyi.RESTAPIStation.models.Sensor;
import ua.pohribnyi.RESTAPIStation.service.SensorService;

@Component
public class MeasurmentValidator implements Validator {

	private final SensorService sensorService;

	@Autowired
	public MeasurmentValidator(SensorService sensorService) {
		this.sensorService = sensorService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Measurement.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Measurement measurment = (Measurement) target;
		Sensor owner = measurment.getSensor();

		if (owner != null && owner.getName().isEmpty()) {
			errors.rejectValue("Sensor.Name", null, "Sensor name should be filled!");
		} else if (sensorService.findSensorByName(owner.getName()).isEmpty()) {
			errors.rejectValue("Sensor.Name", null, "This name of sensor was not found in the database!");
		}

	}

}
