package dk.medicinkortet.fmkdosistiltekstwrapper;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.LinkedList;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.fasterxml.jackson.core.JsonProcessingException;

import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DosageWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.Interval;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.UnitOrUnitsWrapper;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
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
			
			engine = new NashornScriptEngineFactory().getScriptEngine(new String[] {"-ot=false"/*"–-optimistic-types=true --print-code"*/});
			// engine = new ScriptEngineManager().getEngineByName("nashorn");
			Compilable compilingEngine = (Compilable) engine;

			/*
			Bindings bindings = new SimpleBindings();
			bindings.put("console", new DosisTilTekstWrapper().new ConsoleObject());
			engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
			*/
			script = compilingEngine.compile(javascriptFileReader);
			script.eval();
			
			combinedTextConverterObj = engine.eval("dosistiltekst.CombinedTextConverter");
			longTextConverterObj = engine.eval("dosistiltekst.Factory.getLongTextConverter()");
			shortTextConverterObj = engine.eval("dosistiltekst.Factory.getShortTextConverter()");
			dosageTypeCalculatorObj = engine.eval("dosistiltekst.DosageTypeCalculator");
			dosageTypeCalculator144Obj = engine.eval("dosistiltekst.DosageTypeCalculator144");
			dailyDosisCalculatorObj = engine.eval("dosistiltekst.DailyDosisCalculator");
			invocable = (Invocable) engine;
		}
	}
	
	public static DosageTranslationCombined convertCombined(DosageWrapper dosage) {
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
				
		String json = "(unset)";
		ScriptObjectMirror res;
		try {
			json = JSONHelper.toJsonString(dosage);
	        res = (ScriptObjectMirror) invocable.invokeMethod(combinedTextConverterObj, "convertStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in DosisTilTekstWrapper.convertCombined() with json " + json, e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in DosisTilTekstWrapper.convertCombined() with json " + json, e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException inDosisTilTekstWrapper.convertCombined() with json " + json, e);
		}
		
		if(res == null) {
			return new DosageTranslationCombined(new DosageTranslation(null, null, new DailyDosis()), new LinkedList<DosageTranslation>());
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
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		String json = "(unset)";
		Object res;
		try {
			json = JSONHelper.toJsonString(dosage);
	        res = invocable.invokeMethod(longTextConverterObj, "convertStr", json);

		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in DosisTilTekstWrapper.convertLongText() with json " + json, e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in DosisTilTekstWrapper.convertLongText() with json " + json, e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.convertLongText() with json " + json, e);
		}
		
		return (String)res;
	}
	
	public static String convertShortText(DosageWrapper dosage) {
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
			
		Object res;
		String json = "(unset)";
		try {
			json = JSONHelper.toJsonString(dosage);
	        res = invocable.invokeMethod(shortTextConverterObj, "convertStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in DosisTilTekstWrapper.convertShortText() with json " + json, e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in DosisTilTekstWrapper.convertShortText() with json " + json, e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.convertShortText() with json " + json, e);
		}
		
		return (String)res;
	}
	
	public static String convertShortText(DosageWrapper dosage, int maxLength) {
	
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		String json = "(unset)";
		Object res;
		try {
			json = JSONHelper.toJsonString(dosage);
	        res = invocable.invokeMethod(shortTextConverterObj, "convertStr", json, maxLength);

		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in DosisTilTekstWrapper.convertShortText() with json " + json, e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in DosisTilTekstWrapper.convertShortText() with json " + json, e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.convertShortText() with json " + json, e);
		}
		
		return (String)res;
	}
	
	public static String getShortTextConverterClassName(DosageWrapper dosage) {
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		String json = "(unset)";
		Object res;
		try {
			json = JSONHelper.toJsonString(dosage);
			res = engine.eval("dosistiltekst.Factory.getShortTextConverter().getConverterClassName(" + json + ")");
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getShortTextConverterClassName()", e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.getShortTextConverterClassName() with json " + json, e);
		}
		
		return (String)res;
	}
	
	public static String getLongTextConverterClassName(DosageWrapper dosage) {
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		String json = "(unset)";
		Object res;
		try {
			json = JSONHelper.toJsonString(dosage);
			res = engine.eval("dosistiltekst.Factory.getLongTextConverter().getConverterClassName(" + json + ")");
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getLongTextConverterClassName()", e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.getLongTextConverterClassName() with json " + json, e);
		}
		
		return (String)res;
	}
		
	public static DosageType getDosageType(DosageWrapper dosage) {
				
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		String json = "(unset)";
		Object res;
		try {
			json = JSONHelper.toJsonString(dosage);
	        res = invocable.invokeMethod(dosageTypeCalculatorObj, "calculateStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getDosageType()", e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in getDosageType()", e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.getDosageType() with json " + json, e);
		}
		
		return DosageType.fromInteger((Integer)res);
	}
	
	public static DosageType getDosageType144(DosageWrapper dosage) {
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		String json = "(unset)";
		Object res;
		try {
			json = JSONHelper.toJsonString(dosage);
	        res = invocable.invokeMethod(dosageTypeCalculator144Obj, "calculateStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in getDosageType()", e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in getDosageType()", e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.getDosageType144() with json " + json, e);
		}
		
		return DosageType.fromInteger((Integer)res);
	}
	
	public static DailyDosis calculateDailyDosis(DosageWrapper dosage) {
		
		if(engine == null) {
			throw new RuntimeException("DosisTilTekstWrapper not initialized - call initialize() method before invoking any of the methods");
		}
		
		ScriptObjectMirror res;
		String json = "(unset)";
		
		try {
			json = JSONHelper.toJsonString(dosage);
	        res = (ScriptObjectMirror)invocable.invokeMethod(dailyDosisCalculatorObj, "calculateStr", json);
		} catch (ScriptException e) {
			e.printStackTrace();
			throw new RuntimeException("ScriptException in calculateDailyDosis()", e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException in calculateDailyDosis()", e);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("JsonProcessingException in DosisTilTekstWrapper.calculateDailyDosis() with json " + json, e);
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
			if(value instanceof Integer) {
				return new DailyDosis(BigDecimal.valueOf((Integer)value), unitWrapper);
			}
			else if(value instanceof Double) {
				return new DailyDosis(BigDecimal.valueOf((double)value), unitWrapper);
			}
			else {
				throw new RuntimeException("Unexpected type of dailydosis value: " + value);
			}
		}
		else {
			ScriptObjectMirror interval = (ScriptObjectMirror)res.get("interval");
			return new DailyDosis(new Interval<BigDecimal>(BigDecimal.valueOf((double)interval.get("minimum")), BigDecimal.valueOf((double)interval.get("maximum"))), unitWrapper);
		}
	}
}