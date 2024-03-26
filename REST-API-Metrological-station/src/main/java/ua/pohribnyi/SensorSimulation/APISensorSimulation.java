package ua.pohribnyi.SensorSimulation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ua.pohribnyi.RESTAPIStation.dto.MeasurmentDTO;
import ua.pohribnyi.RESTAPIStation.dto.SensorDTO;

public class APISensorSimulation {

	private static final String URL_GET_SENSORS = "/sensors";
	private static final String URL_SENSOR_REGISTRATION = "/sensors/registration";
	private static final String URL_GET_MEASUREMENTS = "/measurments";
	private static final String URL_ADD_MEASUREMENT = "/measurments/add";
	private static final String URL_COUNT_RAINY_DAYS = "/measurments/rainyDaysCount";
	private static final String URL_HOST = "http://localhost:8080";

	private static Random rand = new Random();
	private static Scanner scanner = new Scanner(System.in);

	private StringBuilder sbRouteURL = new StringBuilder(URL_HOST);
	private RestTemplate template = new RestTemplate();

	public static void main(String[] args) {
		APISensorSimulation apiSensorSimulation = new APISensorSimulation();
		apiSensorSimulation.startProgram();
	}

	public void startProgram() {

		showDescriptionOfProgramAPI();

		while (true) {

			sbRouteURL.setLength(URL_HOST.length());
			System.out.println("----------------------------------------------------------");
			System.out.println();
			System.out.println("Select one of option from my main menu:");

			if (scanner.hasNextInt()) {

				int query = scanner.nextInt();

				// should skip the ENTER from previous line
				scanner.nextLine();

				switch (query) {
				case 1: {
					createNewSensor();
					break;
				}
				case 2: {
					getAllSensors();
					break;
				}
				case 3: {
					addNewMeasurments();
					break;
				}
				case 4: {
					addNthRandomMeasurements();
					break;
				}
				case 5: {
					getAllMeasurements();
					break;
				}
				case 6: {
					countRainyDays();
					break;
				}
				case 7: {
					drawATemperatureGraph();
					break;
				}
				case 8: {
					System.out.println("Bye!");
					System.exit(0);
				}
				default:
					throw new IllegalArgumentException(
							"Unexpected option of menu, check the description in a start of program");
				}

			} else {
				System.out.println("Check the description in a start of program and try again");
				break;
			}
		}
		scanner.close();
	}

	private void showDescriptionOfProgramAPI() {
		StringBuilder sbDescription = new StringBuilder();
		sbDescription.append("Hello! I am a program for simulation work of metrological sensor \n");
		sbDescription.append("If you want to use my API you need to choose one of next option: \n");
		sbDescription.append("1. Create new Sensor in metrological database \n");
		sbDescription.append("2. Show all existed Sensor in metrological database \n");
		sbDescription.append("3. Add new Measurements to an existing Sensor \n");
		sbDescription.append("4. Add 1000 random Measurements to an existing Sensor \n");
		sbDescription.append("5. Show all existed Measurements \n");
		sbDescription.append("6. Count the rainy days \n");
		sbDescription.append("7. Draw a temperature graph \n");
		sbDescription.append("8. Exit \n");
		System.out.println(sbDescription.toString());
	}

	public void getAllSensors() {
		ResponseEntity<SensorDTO[]> responseEntity = sendGetForEntity(sbRouteURL.append(URL_GET_SENSORS).toString(),
				SensorDTO[].class);
		System.out.println("Here are the list of all existed Sensors:");
		List<SensorDTO> listSensors = Arrays.asList(responseEntity.getBody());
		System.out.println(listSensors);
	}

	public void createNewSensor() {
		System.out.println("Enter the name for new Sensor:");
		if (scanner.hasNextLine()) {
			createNewSensor(scanner.nextLine());
		} else {
			System.out.println("You should enter the name for create new Sensor!");
		}
	}

	public void createNewSensor(String sensorName) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", sensorName);
		sendPostForEntity(sbRouteURL.append(URL_SENSOR_REGISTRATION).toString(), requestBody);
	}

	public void addNewMeasurments() {
		String sensorName;
		double valueMeasur;
		boolean rainy;

		System.out.println("Enter the name of existing sensor:");
		if (scanner.hasNextLine()) {
			sensorName = scanner.nextLine();
		} else {
			System.out.println("You should enter the name of existing Sensor!");
			return;
		}

		System.out.println("Enter the value of measurments in double format:");
		if (scanner.hasNextDouble()) {
			valueMeasur = scanner.nextDouble();
		} else {
			System.out.println("You should enter the value of measurments in double format!");
			return;
		}

		System.out.println("Enter 'true' if raining now or 'false' otherwise");
		if (scanner.hasNextBoolean()) {
			rainy = scanner.nextBoolean();
		} else {
			System.out.println("You should enter 'true' if raining now or 'false' otherwise!");
			return;
		}
		addNewMeasurements(sensorName, valueMeasur, rainy);
	}

	public void addNewMeasurements(String sensorName, double value, boolean rainy) {
		Map<String, Object> requestBody = new HashMap<>();
		Map<String, String> sensorMap = new HashMap<>();
		sensorMap.put("name", sensorName);

		requestBody.put("sensor", sensorMap);
		requestBody.put("value", value);
		requestBody.put("raining", rainy);

		sendPostForEntity(sbRouteURL.append(URL_ADD_MEASUREMENT).toString(), requestBody);
	}

	public void addNthRandomMeasurements() {
		System.out.println("Enter the name of existing sensor:");
		if (scanner.hasNextLine()) {
			addNthRandomMeasurements(scanner.nextLine(), 1000, sbRouteURL.append(URL_ADD_MEASUREMENT).toString());
		} else {
			System.out.println("You should enter the name of existing Sensor!");
			return;
		}
	}

	public void addNthRandomMeasurements(String sensorName, int measurmentsQuantity, String urlAddMeasur) {
		Map<String, Object> requestBody = new HashMap<>();
		Map<String, String> sensorMap = new HashMap<>();
		int minTemp = -20;
		int maxTemp = 45;
		for (int i = 1; i <= measurmentsQuantity; i++) {
			sensorMap.put("name", sensorName);
			requestBody.put("sensor", sensorMap);
			requestBody.put("value", minTemp + (maxTemp - minTemp) * rand.nextDouble());
			requestBody.put("raining", rand.nextBoolean());
			sendPostForEntity(urlAddMeasur, requestBody);
		}
	}

	public void getAllMeasurements() {
		ResponseEntity<MeasurmentDTO[]> responseEntity = sendGetForEntity(
				sbRouteURL.append(URL_GET_MEASUREMENTS).toString(), MeasurmentDTO[].class);
		System.out.println("Here are the list of all existed Measurements:");
		List<MeasurmentDTO> listMeasurements = Arrays.asList(responseEntity.getBody());
		System.out.println(listMeasurements);
	}

	public List<MeasurmentDTO> getListMeasurements(int pageNumber, int pageSize) {
		sbRouteURL.append(URL_GET_MEASUREMENTS);
		sbRouteURL.append("?page=").append(pageNumber);
		sbRouteURL.append("&page-size=").append(pageSize);
		String urlPageable = sbRouteURL.toString();

		ResponseEntity<MeasurmentDTO[]> responseEntity = sendGetForEntity(urlPageable, MeasurmentDTO[].class);
		List<MeasurmentDTO> listMeasurements = Arrays.asList(responseEntity.getBody());
		return listMeasurements;
	}

	public void countRainyDays() {
		String response = sendGetForObject(sbRouteURL.append(URL_COUNT_RAINY_DAYS).toString(), String.class);
		if (response != null) {
			System.out.println(response.toString());
		}
	}

	public void drawATemperatureGraph() {
		List<MeasurmentDTO> listMeasur = getListMeasurements(0, 1000);
		List<Double> temperature = listMeasur.stream().map(MeasurmentDTO::getValue).collect(Collectors.toList());

		XYChart chart = new XYChartBuilder().width(600).height(500).title("Temperature graph").yAxisTitle("Y").build();
		chart.addSeries("Y", temperature);
		displayXYChart(chart);
	}

	private void displayXYChart(XYChart chart) {
		SwingWrapper<XYChart> sw = new SwingWrapper<>(chart);
		sw.displayChart();
	}

	private <T, V> void sendPostForEntity(String routeUrl, Map<T, V> requestBody) {
		HttpEntity<Map<T, V>> bodyEntity = new HttpEntity<>(requestBody);
		try {
			ResponseEntity<HttpStatus> response = template.postForEntity(routeUrl, bodyEntity, HttpStatus.class);
			System.out.println(response.toString());
		} catch (RestClientException e) {
			System.out.println(e.getMessage());
		}
	}

	private <T> T sendGetForObject(String routeUrl, Class<T> clazz) {
		T response = null;
		try {
			response = template.getForObject(routeUrl, clazz);
		} catch (RestClientException e) {
			System.out.println(e.getMessage());
		}
		return response;
	}

	private <T> ResponseEntity<T> sendGetForEntity(String routeUrl, Class<T> clazz) {
		ResponseEntity<T> responseEntity = template.getForEntity(routeUrl, clazz);
		return responseEntity;
	}

}
