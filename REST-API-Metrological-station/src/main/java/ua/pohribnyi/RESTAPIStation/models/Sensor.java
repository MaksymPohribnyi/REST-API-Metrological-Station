package ua.pohribnyi.RESTAPIStation.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Sensor")
public class Sensor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@NotEmpty(message = "Sensor name should be filled")
	@Column(name = "name")
	@Size(min = 3, max = 30, message = "Sensor name should be between 3 and 30 characters")
	private String name;

	@OneToMany(mappedBy = "sensor")
	private List<Measurement> measurments;

	public Sensor() {

	}

	public Sensor(String name, List<Measurement> measurments) {
		this.name = name;
		this.measurments = measurments;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Measurement> getMeasurments() {
		return measurments;
	}

	public void setMeasurments(List<Measurement> measurments) {
		this.measurments = measurments;
	}

}
