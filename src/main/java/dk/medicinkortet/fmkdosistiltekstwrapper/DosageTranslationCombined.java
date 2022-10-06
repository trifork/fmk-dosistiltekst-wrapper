package dk.medicinkortet.fmkdosistiltekstwrapper;

import java.util.List;

/**
 * This class holds the dosagetranslations for a structured dosage. The combined translation is short and longtext for all periods (if any).
 * The periodtranslations holds each individual period translation (shorttext, longtext and dosagetype)
 */
public class DosageTranslationCombined {

	DosageTranslation combinedTranslation;
	List<DosageTranslation> periodTranslations;

	public DosageTranslationCombined() {
	}

	public DosageTranslationCombined(DosageTranslation combinedTranslation, List<DosageTranslation> periodTranslations) {
		this.combinedTranslation = combinedTranslation;
		this.periodTranslations = periodTranslations;
	}

	public DosageTranslation getCombinedTranslation() {
		return combinedTranslation;
	}

	public List<DosageTranslation> getPeriodTranslations() {
		return periodTranslations;
	}
}