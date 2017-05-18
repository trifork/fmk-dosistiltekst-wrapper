package dk.medicinkortet.fmkdosistiltekstwrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

import dk.medicinkortet.fmkdosistiltekstwrapper.DosisTilTekstWrapper.FMKVersion;

public class DosageProposalXMLGeneratorTest extends DosisTilTekstWrapperTestBase { 
	
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Test
	public void testBasic() throws ParseException {
		DosageProposalResult res = DosisTilTekstWrapper.getDosageProposalResult("PN", 1, "1", "tablet", "tabltter", ", tages med rigeligt vand", SIMPLE_DATE_FORMAT.parse("2017-05-17"), SIMPLE_DATE_FORMAT.parse("2017-06-01"), FMKVersion.FMK146, 1);
		assertNotNull(res);
		assertNotNull(res.getLongText());
		assertEquals("Doseringsforløbet starter onsdag den 17. maj 2017, gentages hver dag, og ophører torsdag den 1. juni 2017:\n" +
				"   Doseringsforløb:\n" + 
				"   1 tablet efter behov højst 1 gang daglig, tages med rigeligt vand", res.getLongText());
		assertEquals("1 tablet efter behov, højst 1 gang daglig, tages med rigeligt vand", res.getShortText());
		assertEquals("<m16:Dosage xsi:schemaLocation=\"http://www.dkma.dk/medicinecard/xml.schema/2015/06/01 ../../../2015/06/01/DosageForRequest.xsd\" xmlns:m16=\"http://www.dkma.dk/medicinecard/xml.schema/2015/06/01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><m16:UnitTexts source=\"Doseringsforslag\"><m16:Singular>tablet</m16:Singular><m16:Plural>tabltter</m16:Plural></m16:UnitTexts><m16:StructuresAccordingToNeed><m16:Structure><m16:IterationInterval>1</m16:IterationInterval><m16:StartDate>2017-04-17</m16:StartDate><m16:EndDate>2017-05-01</m16:EndDate><m16:SupplementaryText>, tages med rigeligt vand</m16:SupplementaryText><m16:Day><m16:Number>1</m16:Number><m16:Dose><m16:Quantity>1</m16:Quantity></m16:Dose></m16:Day></m16:Structure></m16:StructuresAccordingToNeed></m16:Dosage>", res.getXmlSnippet());
	}

}
