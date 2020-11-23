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

import java.util.Arrays;
import java.util.Collection;

public class StructuresWrapper {
	
	private UnitOrUnitsWrapper unitOrUnits;
	private Collection<StructureWrapper> structures;
	private DateOrDateTimeWrapper startDateOrDateTime;
	private DateOrDateTimeWrapper endDateOrDateTime;
	
	public static StructuresWrapper makeStructures(UnitOrUnitsWrapper unitOrUnits, StructureWrapper... structures) {
		return new StructuresWrapper(unitOrUnits, Arrays.asList(structures), null, null);
	}

    public static StructuresWrapper makeStructures(UnitOrUnitsWrapper unitOrUnits, Collection<StructureWrapper> structures) {
		return new StructuresWrapper(unitOrUnits, structures, null, null);
    }
    
    public static StructuresWrapper makeStructures(UnitOrUnitsWrapper unitOrUnits, DateOrDateTimeWrapper startDateOrDateTime, DateOrDateTimeWrapper endDateOrDateTime, StructureWrapper... structures) {
		return new StructuresWrapper(unitOrUnits, Arrays.asList(structures), null, null);
	}

    public static StructuresWrapper makeStructures(UnitOrUnitsWrapper unitOrUnits, DateOrDateTimeWrapper startDateOrDateTime, DateOrDateTimeWrapper endDateOrDateTime, Collection<StructureWrapper> structures) {
		return new StructuresWrapper(unitOrUnits, structures, null, null);
    }
 
	private StructuresWrapper(UnitOrUnitsWrapper unitOrUnits, Collection<StructureWrapper> structures, DateOrDateTimeWrapper startDateOrDateTime, DateOrDateTimeWrapper endDateOrDateTime) {
		this.unitOrUnits = unitOrUnits;
		if(structures==null)
			throw new NullPointerException();
		if(structures.size()==0)
			throw new IllegalArgumentException();
		this.structures = structures;
	}
	
	public UnitOrUnitsWrapper getUnitOrUnits() {
		return unitOrUnits;
	}

	public Collection<StructureWrapper> getStructures() {
		return structures;
	}

	public DateOrDateTimeWrapper getStartDateOrDateTime() {
		return startDateOrDateTime;
	}
	
	public DateOrDateTimeWrapper getEndDateOrDateTime() {
		return endDateOrDateTime;
	}
}
