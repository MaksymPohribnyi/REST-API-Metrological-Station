package ua.pohribnyi.RESTAPIStation.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ua.pohribnyi.RESTAPIStation.dto.SensorDTO;
import ua.pohribnyi.RESTAPIStation.models.Sensor;
import ua.pohribnyi.RESTAPIStation.service.SensorService;
import ua.pohribnyi.RESTAPIStation.util.SensorErrorResponse;
import ua.pohribnyi.RESTAPIStation.util.SensorNotCreatedException;
import ua.pohribnyi.RESTAPIStation.util.SensorValidator;

@RestController
@RequestMapping("/sensors")
public class SensorController {

	private final SensorService sensorService;
	private final ModelMapper modelMapper;
	private final SensorValidator sensorValidator;

	@Autowired
	public SensorController(SensorService sensorService, ModelMapper modelMapper, SensorValidator sensorValidator) {
		this.sensorService = sensorService;
		this.modelMapper = modelMapper;
		this.sensorValidator = sensorValidator;
	}

	@GetMapping
	public List<SensorDTO> getAllSensors() {
		return sensorService.findAllSensors().stream().map(this::convertToSensorDTO).collect(Collectors.toList());
	}

	@PostMapping("/registration")
	public ResponseEntity<HttpStatus> registerNewSensor(@RequestBody @Valid SensorDTO sensorDTO,
			BindingResult bindingResult) {

		Sensor sensor = convertToSensor(sensorDTO);
		
		sensorValidator.validate(sensor, bindingResult);

		if (bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			for (FieldError field : fieldErrors) {
				sb.append(field.getField()).append("-").append(field.getDefaultMessage()).append(";");
			}
			throw new SensorNotCreatedException(sb.toString());
		}

		sensorService.saveSensor(sensor);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	private SensorDTO convertToSensorDTO(Sensor sensor) {
		return modelMapper.map(sensor, SensorDTO.class);
	}

	private Sensor convertToSensor(SensorDTO sensor) {
		return modelMapper.map(sensor, Sensor.class);
	}

	@ExceptionHandler
	ResponseEntity<SensorErrorResponse> sensorNotCreatedHandler(SensorNotCreatedException e) {
		SensorErrorResponse errorResponse = new SensorErrorResponse(e.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<SensorErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}
