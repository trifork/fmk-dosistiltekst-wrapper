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

import java.util.ArrayList;
import java.util.List;

public class DayWrapper {
		
	// Wrapped values
	private int dayNumber;
	private List<DoseWrapper> allDoses = new ArrayList<DoseWrapper>();	

	// Doses were separate types before 2012-06-01. We keep them for now to maintain
	// compatibility in the dosis-to-text conversion
	// AccordingToNeed is merged into each type since 2012-06-01 schemas 
	// private List<AccordingToNeedDoseWrapper> accordingToNeedDoses = new ArrayList<AccordingToNeedDoseWrapper>();
	private List<PlainDoseWrapper> plainDoses = new ArrayList<PlainDoseWrapper>();
	private List<TimedDoseWrapper> timedDoses = new ArrayList<TimedDoseWrapper>();
	private MorningDoseWrapper morningDose;
	private NoonDoseWrapper noonDose;
	private EveningDoseWrapper eveningDose;
	private NightDoseWrapper nightDose;
	
	// Helper / cached values
	private ArrayList<DoseWrapper> accordingToNeedDoses;
	
	private DayWrapper() {
		
	}
	
	public static DayWrapper makeDay(int dayNumber, DoseWrapper... doses) {
		DayWrapper day = new DayWrapper();
		day.dayNumber = dayNumber;
		for(DoseWrapper dose: doses) {
			if(dose!=null) {		
				if(dose instanceof PlainDoseWrapper)
					day.plainDoses.add((PlainDoseWrapper)dose);
				else if(dose instanceof TimedDoseWrapper)
					day.timedDoses.add((TimedDoseWrapper)dose);
				else if(dose instanceof MorningDoseWrapper)
					day.morningDose = (MorningDoseWrapper)dose;
				else if(dose instanceof NoonDoseWrapper)
					day.noonDose = (NoonDoseWrapper)dose;
				else if(dose instanceof EveningDoseWrapper)
					day.eveningDose = (EveningDoseWrapper)dose;
				else if(dose instanceof NightDoseWrapper)
					day.nightDose = (NightDoseWrapper)dose;
				else 
					throw new RuntimeException();
				day.allDoses.add(dose);
			}
		}
		return day;
	}
	
	public int getDayNumber() {
		return dayNumber;
	}

	public int getNumberOfDoses() {
		return allDoses.size();
	}

	public DoseWrapper getDose(int index) {
		return allDoses.get(index);		
	}

	
	public int getNumberOfAccordingToNeedDoses() {
		return getAccordingToNeedDoses().size();
	}
	
	public ArrayList<DoseWrapper> getAccordingToNeedDoses() {
		// Since the 2012/06/01 namespace "according to need" is just a flag
		if(accordingToNeedDoses==null) {
			accordingToNeedDoses = new ArrayList<DoseWrapper>();
			for(DoseWrapper d: allDoses) {
				if(d.isAccordingToNeed())
					accordingToNeedDoses.add(d);
			}
		}
		return accordingToNeedDoses;
	}

	
	public List<PlainDoseWrapper> getPlainDoses() {
		return plainDoses;
	}
	
	public int getNumberOfPlainDoses() {
		return plainDoses.size();
	}	
	
	public MorningDoseWrapper getMorningDose() {
		return morningDose;
	}

	public NoonDoseWrapper getNoonDose() {
		return noonDose;
	}

	public EveningDoseWrapper getEveningDose() {
		return eveningDose;
	}

	public NightDoseWrapper getNightDose() {
		return nightDose;
	}

	public List<DoseWrapper> getAllDoses() {
		return allDoses;		
	}
}
