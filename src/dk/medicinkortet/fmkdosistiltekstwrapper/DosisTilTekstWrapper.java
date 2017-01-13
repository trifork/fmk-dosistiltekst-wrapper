package dk.medicinkortet.fmkdosistiltekstwrapper;

import java.io.Reader;
import java.math.BigDecimal;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import dk.medicinkortet.fmkdosistiltekstwrapper.JSONHelper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DosageWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.Interval;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.UnitOrUnitsWrapper;
import jdk.nashorn.api.scripting.ScriptObjectMirror;


public class DosisTilTekstWrapper {

	public class ConsoleObject {
		public void log(String level, String msg) {
			System.out.println(msg);
		}
	}
	
	private static ScriptEngine engine = null;

	public static void initialize(Reader javascriptFileReader) throws ScriptException {
		
		engine = new ScriptEngineManager().getEngineByName("nashorn");
		Bindings bindings = new SimpleBindings();
		bindings.put("console", new DosisTilTekstWrapper().new ConsoleObject());

		engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
		engine.eval(javascriptFileReader);
	}
	
	public static String convertLongText(DosageWrapper dosage) {
		String json = JSONHelper.toJsonString(dosage);
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		Object res;
		try {
			res = engine.eval("dosistiltekst.Factory.getLongTextConverter().convert(" + json + ")");
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in LongTextConverter.convert() with json " + json, e);
		}
		
		return (String)res;
	}
	
	public static String convertShortText(DosageWrapper dosage) {
		String json = JSONHelper.toJsonString(dosage);
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		Object res;
		try {
			res = engine.eval("dosistiltekst.Factory.getShortTextConverter().convert(" + json + ")");
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in ShortTextConverter.convert() with json " + json, e);
		}
		
		return (String)res;
	}
	
	public static String convertShortText(DosageWrapper dosage, int maxLength) {
		String json = JSONHelper.toJsonString(dosage);
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		
		Object res;
		try {
			res = engine.eval("dosistiltekst.Factory.getShortTextConverter().convert(" + json + "," + maxLength + ")");
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in ShortTextConverter.convert() with json " + json, e);
		}
		
		return (String)res;
	}
	
	public static String getShortTextConverterClassName(DosageWrapper dosage) {
		String json = JSONHelper.toJsonString(dosage);
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		Object res;
		try {
			res = engine.eval("dosistiltekst.Factory.getShortTextConverter().getConverterClassName(" + json + ")");
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getShortTextConverterClassName()", e);
		}
		
		return (String)res;
	}

	public static String getLongTextConverterClassName(DosageWrapper dosage) {
		String json = JSONHelper.toJsonString(dosage);
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		Object res;
		try {
			res = engine.eval("dosistiltekst.Factory.getLongTextConverter().getConverterClassName(" + json + ")");
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getLongTextConverterClassName()", e);
		}
		
		return (String)res;
	}
	
	public static DosageType getDosageType(DosageWrapper dosage) {
		String json = JSONHelper.toJsonString(dosage);
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		Object res;
		try {
			res = engine.eval("dosistiltekst.DosageTypeCalculator.calculate(" + json + ")");
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getDosageType()", e);
		}
		
		return DosageType.fromInteger((Integer)res);
	}
	
	public static DosageType getDosageType144(DosageWrapper dosage) {
		String json = JSONHelper.toJsonString(dosage);
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		Object res;
		try {
			res = engine.eval("dosistiltekst.DosageTypeCalculator144.calculate(" + json + ")");
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getDosageType()", e);
		}
		
		return DosageType.fromInteger((Integer)res);
	}
	
	public static DailyDosis calculateDailyDosis(DosageWrapper dosage) {
		String json = JSONHelper.toJsonString(dosage);
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		ScriptObjectMirror res;
		try {
			res = (ScriptObjectMirror)engine.eval("dosistiltekst.DailyDosisCalculator.calculate(" + json + ")");
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getDosageType()", e);
		}
		
		
		ScriptObjectMirror unitObject = (ScriptObjectMirror)res.get("unitOrUnits");
		if(unitObject == null) {
			return new DailyDosis();
		}
		UnitOrUnitsWrapper unitWrapper;
		if(unitObject.get("unit") != null) {
			unitWrapper = UnitOrUnitsWrapper.makeUnit((String)unitObject.get("unit"));
		}
		else {
			unitWrapper = UnitOrUnitsWrapper.makeUnits((String)unitObject.get("unitSingular"), (String)unitObject.get("unitPlural"));
		}
		Object value = res.get("value");
		if(value != null) {
			return new DailyDosis(BigDecimal.valueOf((double)value), unitWrapper);
		}
		else {
			ScriptObjectMirror interval = (ScriptObjectMirror)res.get("interval");
			return new DailyDosis(new Interval<BigDecimal>(BigDecimal.valueOf((double)interval.get("minimum")), BigDecimal.valueOf((double)interval.get("maximum"))), unitWrapper);
		}
	}
	
	
}
