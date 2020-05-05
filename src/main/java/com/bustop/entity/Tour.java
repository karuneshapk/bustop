package com.bustop.entity;

import com.bustop.entity.enums.CompanyType;
import com.bustop.entity.enums.EfficientRole;
import java.time.LocalTime;

import static com.bustop.entity.enums.CompanyType.*;
import static com.bustop.entity.enums.EfficientRole.DIFFERENT;
import static com.bustop.entity.enums.EfficientRole.EFFICIENT;
import static com.bustop.entity.enums.EfficientRole.NOT_EFFICIENT;

public class Tour {
	private CompanyType companyType;
	private LocalTime   arriveTime;
	private LocalTime   departureTime;

	public Tour(CompanyType companyType, LocalTime arriveTime, LocalTime departureTime) {
		this.companyType = companyType;
		this.arriveTime = arriveTime;
		this.departureTime = departureTime;
	}

	public CompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(CompanyType companyType) {
		this.companyType = companyType;
	}

	public LocalTime getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(LocalTime arriveTime) {
		this.arriveTime = arriveTime;
	}

	public LocalTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(LocalTime departureTime) {
		this.departureTime = departureTime;
	}

	public EfficientRole isEfficientThan(Tour anotherTour) {
		LocalTime   departureTime = anotherTour.getDepartureTime();
		CompanyType companyType   = anotherTour.getCompanyType();
		LocalTime   arriveTime    = anotherTour.getArriveTime();

		boolean doesThisTourStartAtTheSameTimeAndReachEarlier = this.departureTime.equals(departureTime)
			&& this.arriveTime.isBefore(arriveTime);

		boolean doesThisTourStartLaterAndReachAtTheSameTime = this.departureTime.isAfter(departureTime)
			&& this.arriveTime.equals(arriveTime);

		boolean doesThisTourIsBetterByType = this.departureTime.equals(departureTime)
			&& this.arriveTime.equals(arriveTime)
			&& POSH.equals(this.companyType)
			&& GROTTY.equals(companyType);

		if (doesThisTourStartAtTheSameTimeAndReachEarlier || doesThisTourStartLaterAndReachAtTheSameTime || doesThisTourIsBetterByType) {
			return EFFICIENT;
		}


		boolean doesAnotherTourStartAtTheSameTimeAndReachEarlier = departureTime.equals(this.departureTime)
			&& arriveTime.isBefore(this.arriveTime);

		boolean doesAnotherTourStartLaterAndReachAtTheSameTime = departureTime.isAfter(this.departureTime)
			&& arriveTime.equals(this.arriveTime);

		boolean doesAnotherTourIsBetterByType = this.departureTime.equals(departureTime)
			&& this.arriveTime.equals(arriveTime)
			&& POSH.equals(companyType)
			&& GROTTY.equals(this.companyType);

		if (doesAnotherTourIsBetterByType || doesAnotherTourStartAtTheSameTimeAndReachEarlier || doesAnotherTourStartLaterAndReachAtTheSameTime) {
			return NOT_EFFICIENT;
		}


		return DIFFERENT;
	}

	@Override
	public String toString() {
		return "" + companyType + " " + departureTime + " " + arriveTime + "\n";
	}
}
