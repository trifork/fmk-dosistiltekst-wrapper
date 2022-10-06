package dk.medicinkortet.fmkdosistiltekstwrapper;

public class DosageProposalResult {
	private String xmlSnippet;
	private String shortText;
	private String longText;

	public DosageProposalResult() {
	}

	public DosageProposalResult(String xmlSnippet, String shortText, String longText) {
		this.xmlSnippet = xmlSnippet;
		this.shortText = shortText;
		this.longText = longText;
	}

	public String getXmlSnippet() {
		return xmlSnippet;
	}

	public String getShortText() {
		return shortText;
	}

	public String getLongText() {
		return longText;
	}
}
