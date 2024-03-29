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
import java.math.RoundingMode;

public abstract class DoseWrapper {
	
	// Wrapped values
	private BigDecimal minimalDoseQuantity;
	private BigDecimal maximalDoseQuantity;
	private BigDecimal doseQuantity;
	
	private String minimalDoseQuantityString;
	private String maximalDoseQuantityString;
	private String doseQuantityString;
	
	private boolean isAccordingToNeed;
	
	protected DoseWrapper(
			BigDecimal doseQuantity, BigDecimal minimalDoseQuantity, BigDecimal maximalDoseQuantity,  
			boolean isAccordingToNeed) {
		this.doseQuantity = doseQuantity;
		this.minimalDoseQuantity = minimalDoseQuantity; 
		this.maximalDoseQuantity = maximalDoseQuantity;
		this.isAccordingToNeed = isAccordingToNeed;
		if(minimalDoseQuantity!=null) 
			minimalDoseQuantityString = trim(minimalDoseQuantity.toPlainString().replace('.', ',')); 
		if(maximalDoseQuantity!=null)
			maximalDoseQuantityString = trim(maximalDoseQuantity.toPlainString().replace('.', ','));
		if(doseQuantity!=null)
			doseQuantityString = trim(doseQuantity.toPlainString().replace('.', ',')).replace(".", ",");
	}

	private static String trim(String number) {
		if(number.indexOf('.')<0 && number.indexOf(',')<0)
			return number;
		if(number.length()==1 || number.charAt(number.length()-1)>'0')
			return number;
		else 
			return trim(number.substring(0, number.length()-1));
	}
	
	public static BigDecimal toBigDecimal(Double value) {
		if(value==null)
			return null;
		BigDecimal v = new BigDecimal(value);
		v = v.setScale(9, RoundingMode.HALF_UP);
		return v;		
	}
	    
	protected static boolean isZero(BigDecimal quantity) {
		if(quantity==null)
			return true;
		else
			return quantity.doubleValue()<0.000000001;
	}	
	
	protected static boolean isZero(BigDecimal minimalQuantity, BigDecimal maximalQuantity) {
		return minimalQuantity == null && maximalQuantity == null;
	}	
	
	public BigDecimal getMinimalDoseQuantity() {
		return minimalDoseQuantity;
	}

	public BigDecimal getMaximalDoseQuantity() {
		return maximalDoseQuantity;
	}

	public BigDecimal getDoseQuantity() {
		return doseQuantity;
	}

	public String getMinimalDoseQuantityString() {
		return minimalDoseQuantityString;
	}

	public String getMaximalDoseQuantityString() {
		return maximalDoseQuantityString;
	}

	public String getDoseQuantityString() {
		return doseQuantityString;
	}
		
	public boolean isAccordingToNeed() {
		return isAccordingToNeed;
	}
}
