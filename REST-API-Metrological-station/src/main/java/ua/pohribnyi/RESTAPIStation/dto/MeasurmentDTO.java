package ua.pohribnyi.RESTAPIStation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MeasurmentDTO {

	@NotNull(message = "Sensor measurment shouldn`t be empty")
	@Min(value = -100, message = "Sensor measurment shouldn`t be lower than -100")
	@Max(value = 100, message = "Sensor measurment shouldn`t be greater than 100")
	private double value;

	@NotNull(message = "The raining field should be filled in")
	private boolean raining;

	@NotNull(message = "The sensor field should be filled in")
	private SensorDTO sensor;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public SensorDTO getSensor() {
		return sensor;
	}

	public void setSensor(SensorDTO sensor) {
		this.sensor = sensor;
	}

	public boolean isRaining() {
		return raining;
	}

	public void setRaining(boolean raining) {
		this.raining = raining;
	}

	@Override
	public String toString() {
		return "Measurment [value=" + value + ", raining=" + raining + ", sensor=" + sensor + "]";
	}
	
	

}
