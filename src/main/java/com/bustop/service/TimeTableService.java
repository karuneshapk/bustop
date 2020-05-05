package com.bustop.service;

import com.bustop.entity.Tour;
import com.bustop.entity.enums.CompanyType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

@Service
public class TimeTableService {

	@Autowired
	private ConverterService converterService;

	public List<String> getLinesFromFile(Resource res) throws IOException {
		return readAllLines(get(res.getURI()), UTF_8);
	}

	public void generateTimeTable(Resource res) throws IOException {
		List<String> linesFromFile = getLinesFromFile(res);
		List<String> efficientTours = createEfficientTours(linesFromFile);
		createResultTimeTableFile(efficientTours);
	}

	public List<String> createEfficientTours(List<String> lines)  {
		List<Tour> poshEfficientTours   = new ArrayList<>();
		List<Tour> grottyEfficientTours = new ArrayList<>();

		collectOnlyEfficientTours(lines).forEach(tour -> (
			CompanyType.POSH.equals(tour.getCompanyType())
				? poshEfficientTours
				: grottyEfficientTours
		).add(tour));

		return combineAndConvertToStringList(poshEfficientTours, grottyEfficientTours);
	}


	public void createResultTimeTableFile(List<String> results)  throws IOException {
		Files.write(Paths.get("src/main/resources/results/" + LocalDateTime.now() + ".txt"),
			results,
			StandardCharsets.UTF_8,
			StandardOpenOption.CREATE,
			StandardOpenOption.APPEND);
	}

	private List<String> combineAndConvertToStringList(List<Tour> poshList, List<Tour> grottyList) {
		List<String> poshListResult = poshList.stream()
			.sorted(comparing(Tour::getDepartureTime))
			.map(Tour::toString)
			.collect(toList());

		List<String> grottyListResult = grottyList.stream()
			.sorted(comparing(Tour::getDepartureTime))
			.map(Tour::toString)
			.collect(toList());


		if (!grottyListResult.isEmpty() && !poshListResult.isEmpty()) {
			poshListResult.add("");
		}

		poshListResult.addAll(grottyListResult);

		return poshListResult;
	}

	private Queue<Tour> collectOnlyEfficientTours(List<String> lines) {
		Queue<Tour> commonEfficientTours = new LinkedList<>();

		lines.stream()
			.map(line -> converterService.toTour(line))
			.filter(Objects::nonNull)
			.sorted(comparing(Tour::getDepartureTime))
			.forEach(tour -> leaveNotEfficientTour(tour, commonEfficientTours));

		return commonEfficientTours;
	}

	private void leaveNotEfficientTour(Tour tour, Queue<Tour> commonEfficientTours) {
		if (commonEfficientTours.isEmpty()) {
			commonEfficientTours.add(tour);
			return;
		}

		Tour headOfQueue = commonEfficientTours.poll();

		switch (tour.isEfficientThan(headOfQueue)) {
			case EFFICIENT:
				commonEfficientTours.add(tour);
				break;
			case NOT_EFFICIENT:
				commonEfficientTours.add(headOfQueue);
				break;
			case DIFFERENT:
				commonEfficientTours.add(tour);
				commonEfficientTours.add(headOfQueue);
				break;
		}
	}

}
