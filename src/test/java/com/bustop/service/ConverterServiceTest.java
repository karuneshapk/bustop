package com.bustop.service;

import com.bustop.entity.Tour;
import com.bustop.entity.enums.CompanyType;
import com.bustop.service.ConverterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ConverterServiceTest {

	@InjectMocks
	ConverterService converterService = new ConverterService();

	@Test
	public void toTour_WhenEqualsAtOneHour_ShouldTourObject() {
		Tour tour = converterService.toTour("posh 01:00 02:00");
		assertThat(tour, notNullValue());
	}

	@Test
	public void toTour_WhenLongerThanOneHour_ShouldReturnNull() {
		Tour tour = converterService.toTour("posh 01:00 02:01");
		assertThat(tour, nullValue());
	}

	@Test
	public void toTour_WhenIsLessThanOneHour_ShouldReturnTourObject() {
		Tour tour = converterService.toTour("posh 01:00 01:59");
		assertThat(tour, notNullValue());
	}

	@Test
	public void toTour_WhenHourStringFormatIsNotFull_ShouldReturnCorrectTourObject() {
		Tour tour = converterService.toTour("posh 1:30 2:25");

		assertThat(tour.getCompanyType(),             is(CompanyType.POSH));
		assertThat(tour.getDepartureTime().getHour(), is(1));
		assertThat(tour.getArriveTime().getHour(),    is(2));
	}
}
