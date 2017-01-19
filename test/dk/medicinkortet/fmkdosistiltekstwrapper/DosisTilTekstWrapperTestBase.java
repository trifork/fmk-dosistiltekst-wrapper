package dk.medicinkortet.fmkdosistiltekstwrapper;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptException;

import org.junit.Before;

public abstract class DosisTilTekstWrapperTestBase {

	@Before
	public void setUp() throws FileNotFoundException, ScriptException {
		DosisTilTekstWrapper.initialize(new FileReader("../fmk-dosis-til-tekst-ts/target/dosistiltekst.js"));
	}
}
