package dk.medicinkortet.fmkdosistiltekstwrapper;

/**
 * A combination of shorttext, longtext and dailydosis for a structured dosage (either combined or for one period)
 *
 */
public class DosageTranslation {
	String shortText;
	String longText;
	DailyDosis dailyDosis;
	
	public String getShortText() {
		return shortText;
	}
	
	public String getLongText() {
		return longText;
	}
	
	public DailyDosis getDailyDosis() {
		return dailyDosis;
	}
	
	public DosageTranslation(String shortText, String longText, DailyDosis dailyDosis) {
		this.shortText = shortText;
		this.longText = longText;
		this.dailyDosis = dailyDosis;
	}
}