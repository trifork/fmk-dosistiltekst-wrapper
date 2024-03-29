package dk.medicinkortet.fmkdosistiltekstwrapper;


import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.*;

import java.io.IOException;

public class JSONHelper {
	static ObjectMapper mapper = null;
	
	public static class DateOrDateTimeWrapperSerializer extends StdSerializer<DateOrDateTimeWrapper> {

		public DateOrDateTimeWrapperSerializer() {
	        this(null);
	    }
	   
	    public DateOrDateTimeWrapperSerializer(Class<DateOrDateTimeWrapper> t) {
	        super(t);
	    }

		@Override
		public void serialize(DateOrDateTimeWrapper dateOrDateTime, JsonGenerator jgen, SerializerProvider provider)
				throws IOException {
			jgen.writeStartObject();
			
			if(dateOrDateTime.getDate() != null) {
				jgen.writeNumberField("date", dateOrDateTime.getDate().getTime()); //new SimpleDateFormat("yyyy-MM-dd").format(dateOrDateTime.getDate())); // "startDateOrDateTime":{"date":"2014-02-07"}
			}
			else {
				jgen.writeNumberField("dateTime", dateOrDateTime.getDateTime().getTime()); //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateOrDateTime.getDateTime())); // "startDateOrDateTime":{"dateTime":"2014-02-07"}
			}
			
			jgen.writeEndObject();
		}
	}

	public static class DoseWrapperSerializer extends StdSerializer<DoseWrapper> {
		  

		public DoseWrapperSerializer() {
	        this(null);
	    }
	   
	    public DoseWrapperSerializer(Class<DoseWrapper> t) {
	        super(t);
	    }

		@Override
		public void serialize(DoseWrapper dose, JsonGenerator jgen, SerializerProvider provider) throws IOException {
			
			jgen.writeStartObject();
			
			if(dose.getDoseQuantity() != null) {
				jgen.writeObjectField("doseQuantity", dose.getDoseQuantity());
			}
			
			if(dose.getMinimalDoseQuantity() != null) {
				jgen.writeObjectField("minimalDoseQuantity", dose.getMinimalDoseQuantity());
			}
			
			if(dose.getMaximalDoseQuantity() != null) {
				jgen.writeObjectField("maximalDoseQuantity", dose.getMaximalDoseQuantity());
			}
			
			if(dose.getDoseQuantityString() != null) {
				jgen.writeStringField("doseQuantityString", dose.getDoseQuantityString());
			}
			
			if(dose.getMinimalDoseQuantityString() != null) {
				jgen.writeStringField("minimalDoseQuantityString", dose.getMinimalDoseQuantityString());
			}
			
			if(dose.getMaximalDoseQuantityString() != null) {
				jgen.writeStringField("maximalDoseQuantityString", dose.getMaximalDoseQuantityString());
			}
			
			jgen.writeBooleanField("isAccordingToNeed", dose.isAccordingToNeed());
			
			if(dose instanceof MorningDoseWrapper) {
				jgen.writeStringField("type", "MorningDoseWrapper");
			}
			else if(dose instanceof NoonDoseWrapper) {
				jgen.writeStringField("type", "NoonDoseWrapper");
			}
			else if(dose instanceof EveningDoseWrapper) {
				jgen.writeStringField("type", "EveningDoseWrapper");
			}
			else if(dose instanceof NightDoseWrapper) {
				jgen.writeStringField("type", "NightDoseWrapper");
			}
			else if(dose instanceof PlainDoseWrapper) {
				jgen.writeStringField("type", "PlainDoseWrapper");
			}
			else if(dose instanceof TimedDoseWrapper) {
				TimedDoseWrapper timedDose = (TimedDoseWrapper)dose;
				jgen.writeStringField("type", "TimedDoseWrapper");
				jgen.writeObjectFieldStart("time");
				jgen.writeNumberField("hour", timedDose.getTime().getHour());
				jgen.writeNumberField("minute", timedDose.getTime().getMinute());
				if(timedDose.getTime().getSecond() != null) {
					jgen.writeNumberField("second", timedDose.getTime().getSecond());
				}
				jgen.writeEndObject();
			}
			
			jgen.writeEndObject();
		}
	}

	public static class DayWrapperSerializer extends StdSerializer<DayWrapper> {


		public DayWrapperSerializer() {
			this(null);
		}

		public DayWrapperSerializer(Class<DayWrapper> t) {
			super(t);
		}

		@Override
		public void serialize(DayWrapper day, JsonGenerator jgen, SerializerProvider provider) throws IOException {

			jgen.writeStartObject();

			jgen.writeObjectField("dayNumber", day.getDayNumber());

			if(day.getAllDoses() != null) {
				jgen.writeObjectField("allDoses", day.getAllDoses());
			}

			jgen.writeEndObject();
		}
	}

	private static ObjectMapper getObjectMapper() {
		if(mapper == null) {
			mapper = new ObjectMapper();
			
			SimpleModule module = new SimpleModule();
			module.addSerializer(DateOrDateTimeWrapper.class, new JSONHelper.DateOrDateTimeWrapperSerializer());
			module.addSerializer(DoseWrapper.class, new JSONHelper.DoseWrapperSerializer());
			module.addSerializer(DayWrapper.class, new JSONHelper.DayWrapperSerializer());
			mapper.registerModule(module);
			mapper.registerModule(new JavaTimeModule());
			mapper.setSerializationInclusion(Include.NON_NULL);
		}
		
		return mapper;
	}
	
	public static String toJsonString(Object object) throws JsonProcessingException {		
		
		return getObjectMapper().writeValueAsString(object);
	}
}
