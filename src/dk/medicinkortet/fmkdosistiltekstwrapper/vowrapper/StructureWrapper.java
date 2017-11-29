/**
* The contents of this file are subject to the Mozilla Public
* License Version 1.1 (the "License"); you may not use this file
* except in compliance with the License. You may obtain a copy of
* the License at http://www.mozilla.org/MPL/
*
* Software distributed under the License is distributed on an "AS
* IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
* implied. See the License for the specific language governing
* rights and limitations under the License.
*
* Contributor(s): Contributors are attributed in the source code
* where applicable.
*
* The Original Code is "Dosis-til-tekst".
*
* The Initial Developer of the Original Code is Trifork Public A/S.
*
* Portions created for the FMK Project are Copyright 2011,
* National Board of e-Health (NSI). All Rights Reserved.
*/

package dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class StructureWrapper {
	
	// Mapped values
	private int iterationInterval;
	private String supplText;	
	private DateOrDateTimeWrapper startDateOrDateTime;
	private DateOrDateTimeWrapper endDateOrDateTime;
	private List<DayWrapper> days;
	private Object refToSource;
	private String dosagePeriodPostfix;
	
	/**
	 * Factory metod to create structured dosages
	 */
	public static StructureWrapper makeStructure(int iterationInterval, String supplText, DateOrDateTimeWrapper startDateOrDateTime, DateOrDateTimeWrapper endDateOrDateTime, DayWrapper... days) {
		return new StructureWrapper(iterationInterval, supplText, startDateOrDateTime, endDateOrDateTime, Arrays.asList(days), null);
	}
	
	
	public static StructureWrapper makeStructure(int iterationInterval, String supplText, DateOrDateTimeWrapper startDateOrDateTime, DateOrDateTimeWrapper endDateOrDateTime, List<DayWrapper> days) {
		return new StructureWrapper(iterationInterval, supplText, startDateOrDateTime, endDateOrDateTime, days, null);
	}
	
	private StructureWrapper(int iterationInterval, String supplText, 
			DateOrDateTimeWrapper startDateOrDateTime, DateOrDateTimeWrapper endDateOrDateTime,
			List<DayWrapper> days,
			Object refToSource) {
		this.iterationInterval = iterationInterval;
		this.supplText = supplText;
		this.startDateOrDateTime = startDateOrDateTime;
		this.endDateOrDateTime = endDateOrDateTime;
		if(days==null)
			throw new NullPointerException();

		this.days = days;
		this.refToSource = refToSource;
	}

	public int getIterationInterval() {
		return iterationInterval;
	}

	public String getSupplText() {
		return supplText;
	}

	public void setSupplText(String supplText) {
	    this.supplText = supplText;
	}
	
	public DateOrDateTimeWrapper getStartDateOrDateTime() {
		return startDateOrDateTime;
	}

	public DateOrDateTimeWrapper getEndDateOrDateTime() {
		return endDateOrDateTime;
	}
	
	public Object getRefToSource() {
		return refToSource;
	}
	
	public String getDosagePeriodPostfix() {
	    return dosagePeriodPostfix;
	}
	
	public void setDosagePeriodPostfix(String dosagePeriodPostfix) {
	    this.dosagePeriodPostfix = dosagePeriodPostfix;
	}
	
	public boolean startsAndEndsSameDay() {
		if(getStartDateOrDateTime()==null || getEndDateOrDateTime()==null)
			return false;
		GregorianCalendar cal1 = new GregorianCalendar();
		cal1.setTime(getStartDateOrDateTime().getDateOrDateTime());
		GregorianCalendar cal2 = new GregorianCalendar();
		cal2.setTime(getEndDateOrDateTime().getDateOrDateTime());
		return cal1.get(GregorianCalendar.YEAR) == cal2.get(GregorianCalendar.YEAR) &&
				cal1.get(GregorianCalendar.DAY_OF_YEAR) == cal2.get(GregorianCalendar.DAY_OF_YEAR);
}	
}
