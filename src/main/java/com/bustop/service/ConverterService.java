package com.bustop.service;

import com.bustop.entity.Tour;
import com.bustop.entity.enums.CompanyType;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

import static com.bustop.entity.enums.CompanyType.valueOf;
import static java.time.LocalTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;

@Service
public class ConverterService {
	private static final DateTimeFormatter FORMATTER  = ofPattern("HH:mm");
	private static final Long              ONE_HOUR   = 1L;


	public Tour toTour(String line) {
		String[] datas = line.split("\\s+");

		if (datas.length != 3) {
			return null;
		}

		LocalTime   departureTime = parse(validateStringFormat(datas[1]), FORMATTER);
		LocalTime   arriveTime    = parse(validateStringFormat(datas[2]), FORMATTER);

		if (isLongerThanOneHour(departureTime, arriveTime)) {
			return null;
		}

		CompanyType companyType = valueOf(datas[0].toUpperCase());

		if (arriveTime.isBefore(departureTime)) {
			return null;
		}

		return new Tour(companyType, arriveTime, departureTime);
	}


	private String validateStringFormat(String stringData) {
		String[] timeDatas = stringData.split(":");

		String   hh  = timeDatas[0];
		String   mm  = timeDatas[1];

		hh = hh.length() < 2 ? "0" + hh : hh;
		mm = mm.length() < 2 ? "0" + mm : mm;

		return hh + ":" + mm;
	}

	private boolean isLongerThanOneHour(LocalTime departureTime, LocalTime arriveTime) {
		return departureTime.isBefore(arriveTime.minusHours(ONE_HOUR));
	}
}
