package dk.medicinkortet.fmkdosistiltekstwrapper;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.LinkedList;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
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
	private static CompiledScript script;
	private static Invocable invocable;
	private static Object combinedTextConverterObj;
	private static Object shortTextConverterObj;
	private static Object longTextConverterObj;
	private static Object dosageTypeCalculatorObj;
	private static Object dosageTypeCalculator144Obj;
	private static Object dailyDosisCalculatorObj;
	
	
	public static void initialize(Reader javascriptFileReader) throws ScriptException {
		if(engine == null) {
			engine = new ScriptEngineManager().getEngineByName("nashorn");
			Compilable compilingEngine = (Compilable) engine;
			
			script = compilingEngine.compile(javascriptFileReader);
			script.eval();
			
			combinedTextConverterObj = engine.eval("dosistiltekst.CombinedTextConverter");
			longTextConverterObj = engine.eval("dosistiltekst.Factory.getLongTextConverter()");
			shortTextConverterObj = engine.eval("dosistiltekst.Factory.getShortTextConverter()");
			dosageTypeCalculatorObj = engine.eval("dosistiltekst.DosageTypeCalculator");
			dosageTypeCalculator144Obj = engine.eval("dosistiltekst.DosageTypeCalculator144");
			dailyDosisCalculatorObj = engine.eval("dosistiltekst.DailyDosisCalculator");
			invocable = (Invocable) engine;

			Bindings bindings = new SimpleBindings();
			bindings.put("console", new DosisTilTekstWrapper().new ConsoleObject());
			engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
		}
	}
	
	public static DosageTranslationCombined convertCombined(DosageWrapper dosage) {
		String json = JSONHelper.toJsonString(dosage);
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
				
		ScriptObjectMirror res;
		try {
			
	        res = (ScriptObjectMirror) invocable.invokeMethod(combinedTextConverterObj, "convertStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in LongTextConverter.convert() with json " + json, e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in LongTextConverter.convert() with json " + json, e);
		}
		
		String combinedShortText = (String)res.get("combinedShortText");
		String combinedLongText = (String)res.get("combinedLongText");
		DailyDosis combinedDD = getDailyDosisFromJS((ScriptObjectMirror)res.get("combinedDailyDosis"));
		ScriptObjectMirror  periodTexts = (ScriptObjectMirror)res.get("periodTexts");
		ScriptObjectMirror[] periodTextArray = periodTexts.to(ScriptObjectMirror[].class);
		LinkedList<DosageTranslation> translations = new LinkedList<DosageTranslation>(); 
		for(ScriptObjectMirror periodText: periodTextArray) {
			translations.add(new DosageTranslation((String)periodText.getSlot(0), (String)periodText.getSlot(1), getDailyDosisFromJS((ScriptObjectMirror) periodText.getSlot(2))));
		}
		return new DosageTranslationCombined(new DosageTranslation(combinedShortText, combinedLongText, combinedDD), translations);
	}
		
	public static String convertLongText(DosageWrapper dosage) {
		String json = JSONHelper.toJsonString(dosage);
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		Object res;
		try {
	        res = invocable.invokeMethod(longTextConverterObj, "convertStr", json);

		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in LongTextConverter.convert() with json " + json, e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in LongTextConverter.convert() with json " + json, e);
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
	        res = invocable.invokeMethod(shortTextConverterObj, "convertStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in ShortTextConverter.convert() with json " + json, e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in ShortTextConverter.convert() with json " + json, e);
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
	        res = invocable.invokeMethod(shortTextConverterObj, "convertStr", json, maxLength);

		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in ShortTextConverter.convert() with json " + json, e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in ShortTextConverter.convert() with json " + json, e);
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
	        res = invocable.invokeMethod(dosageTypeCalculatorObj, "calculateStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getDosageType()", e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in getDosageType()", e);
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
	        res = invocable.invokeMethod(dosageTypeCalculator144Obj, "calculateStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getDosageType()", e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in getDosageType()", e);
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
	        res = (ScriptObjectMirror)invocable.invokeMethod(dailyDosisCalculatorObj, "calculateStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getDosageType()", e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in getDosageType()", e);
		}
		
		return getDailyDosisFromJS(res);
	}

	private static DailyDosis getDailyDosisFromJS(ScriptObjectMirror res) {
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