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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ua.pohribnyi.RESTAPIStation.dto.MeasurmentDTO;
import ua.pohribnyi.RESTAPIStation.models.Measurement;
import ua.pohribnyi.RESTAPIStation.service.MeasurmentService;
import ua.pohribnyi.RESTAPIStation.util.MeasurmentAddingException;
import ua.pohribnyi.RESTAPIStation.util.MeasurmentErrorResponse;
import ua.pohribnyi.RESTAPIStation.util.MeasurmentValidator;

@RestController
@RequestMapping("/measurments")
public class MeasurementController {

	private final MeasurmentService measurmentService;
	private final ModelMapper modelMapper;
	private final MeasurmentValidator measurmentValidator;

	@Autowired
	public MeasurementController(MeasurmentService measurmentService, ModelMapper modelMapper,
			MeasurmentValidator measurmentValidator) {
		this.measurmentService = measurmentService;
		this.modelMapper = modelMapper;
		this.measurmentValidator = measurmentValidator;
	}

	@GetMapping
	public List<MeasurmentDTO> getAllMeasurments(@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "page-size", required = false) Integer pageSize) {
		if (page != null && pageSize != null) {
			return measurmentService.findAllMeasurmentsPageable(page, pageSize).stream()
					.map(this::convertToMeasurmentDTO).collect(Collectors.toList());
		}
		return measurmentService.findAllMeasurments().stream().map(this::convertToMeasurmentDTO)
				.collect(Collectors.toList());
	}

	@PostMapping("/add")
	public ResponseEntity<HttpStatus> addNewMeasurment(@RequestBody @Valid MeasurmentDTO measurmentDTO,
			BindingResult bindingResult) {

		Measurement currMeasurment = convertToMeasurment(measurmentDTO);

		measurmentValidator.validate(currMeasurment, bindingResult);

		if (bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			for (FieldError field : fieldErrors) {
				sb.append(field.getField()).append("-").append(field.getDefaultMessage()).append(";");
			}
			throw new MeasurmentAddingException(sb.toString());
		}
		measurmentService.saveMeasurment(currMeasurment);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

	@GetMapping("/rainyDaysCount")
	public String countRainyDays() {
		StringBuilder sb = new StringBuilder();
		sb.append("Metrological station sensors counted: ");
		sb.append(measurmentService.countRainyDays());
		sb.append(" rainy day(s) over the entire period");
		return sb.toString();
	}

	private MeasurmentDTO convertToMeasurmentDTO(Measurement measurment) {
		modelMapper.typeMap(Measurement.class, MeasurmentDTO.class).addMapping(Measurement::isItsRaining,
				MeasurmentDTO::setRaining);
		return modelMapper.map(measurment, MeasurmentDTO.class);
	}

	private Measurement convertToMeasurment(MeasurmentDTO measurmentDTO) {
		modelMapper.typeMap(MeasurmentDTO.class, Measurement.class).addMapping(MeasurmentDTO::isRaining,
				Measurement::setItsRaining);
		return modelMapper.map(measurmentDTO, Measurement.class);
	}

	@ExceptionHandler
	public ResponseEntity<MeasurmentErrorResponse> measurmentAddingHandler(MeasurmentAddingException e) {
		MeasurmentErrorResponse errorResponse = new MeasurmentErrorResponse(e.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<MeasurmentErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}
