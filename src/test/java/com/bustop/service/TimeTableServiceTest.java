package com.bustop.service;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimeTableServiceTest {
	private static final String SEPARATOR = "";

	@InjectMocks
	private TimeTableService timeTableService = new TimeTableService();

	@Mock
	private ConverterService converterService;

	@Test
	public void createEfficientToursWithBothCompanyTypes() {
		when(converterService.toTour(any())).thenCallRealMethod();

		List<String> lines = asList("Posh 10:10 11:00",
			"Grotty 12:45 13:25",
			"Grotty 12:30 13:25",
			"Posh 12:05 12:30",
			"Grotty 10:10 11:00",
			"Posh 17:25 18:01",
			"Posh 10:15 11:10",
			"Grotty 16:30 18:45");

		List<String> efficientTours = timeTableService.createEfficientTours(lines);
		assertThat(efficientTours, hasSize(7));
		assertThat(efficientTours.get(0), is("POSH 10:10 11:00\n"));
		assertThat(efficientTours.get(1), is("POSH 10:15 11:10\n"));
		assertThat(efficientTours.get(2), is("POSH 12:05 12:30\n"));
		assertThat(efficientTours.get(3), is("POSH 17:25 18:01\n"));
		assertThat(efficientTours.get(4), is(SEPARATOR));
		assertThat(efficientTours.get(5), is("GROTTY 12:30 13:25\n"));
		assertThat(efficientTours.get(6), is("GROTTY 12:45 13:25\n"));
	}

	@Test
	public void removeAllTheSameGrottyAsPosh() {
		when(converterService.toTour(any())).thenCallRealMethod();

		List<String> lines = asList("Posh 10:10 11:00", "Grotty 10:10 11:00", "Grotty 16:30 17:29", "Posh 16:30 17:29");

		List<String> efficientTours = timeTableService.createEfficientTours(lines);
		assertThat(efficientTours, hasSize(2));
		assertThat(efficientTours.get(0), is("POSH 10:10 11:00\n"));
		assertThat(efficientTours.get(1), is("POSH 16:30 17:29\n"));
	}

	@Test
	public void removeAllLongerThanOneHour() {
		when(converterService.toTour(any())).thenCallRealMethod();

		List<String> lines = asList("Posh 9:10 11:20", "Grotty 9:10 10:20", "Grotty 10:10 11:00");

		List<String> efficientTours = timeTableService.createEfficientTours(lines);
		assertThat(efficientTours, hasSize(1));
		assertThat(efficientTours.get(0), is("GROTTY 10:10 11:00\n"));
	}


	@Test
	public void createEfficientTour_WhenOneContainsAnotherTour() {
		when(converterService.toTour(any())).thenCallRealMethod();

		List<String> lines = asList("Posh 10:10 10:50", "Posh 15:00 15:10", "Posh 10:30 10:40");

		List<String> efficientTours = timeTableService.createEfficientTours(lines);
		assertThat(efficientTours, hasSize(2));
		assertThat(efficientTours.get(0), is("POSH 10:30 10:40\n"));
		assertThat(efficientTours.get(1), is("POSH 15:00 15:10\n"));
	}
}
