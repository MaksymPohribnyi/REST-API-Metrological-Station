package ua.pohribnyi.RESTAPIStation.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Sensor_measurments")
public class Measurement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "measurment")
	@NotNull(message = "Sensor measurment shouldn`t be empty")
	@Min(value = -100, message = "Sensor measurment shouldn`t be lower than -100")
	@Max(value = 100, message = "Sensor measurment shouldn`t be greater than 100")
	private double value;

	@Column(name = "raining")
	@NotNull(message = "The raining field should be filled in")
	private boolean itsRaining;

	@ManyToOne
	@JoinColumn(name = "sensor_id", referencedColumnName = "id")
	@NotNull(message = "The sensor field should be filled in")
	private Sensor sensor;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public Measurement() {

	}

	public Measurement(double value, boolean itsRaining, Sensor sensor, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.value = value;
		this.itsRaining = itsRaining;
		this.sensor = sensor;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public boolean isItsRaining() {
		return itsRaining;
	}

	public void setItsRaining(boolean itsRaining) {
		this.itsRaining = itsRaining;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
