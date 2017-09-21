package com.sdm.core.filter;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdm.core.Globalizer;

@Provider
public class JsonResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper jsonMapper = Globalizer.jsonMapper();

    /*
	 * private Map<String, String> validateRequest(Class<I> reqClass, I request) {
	 * Map<String, String> errors = new HashMap<>();
	 * 
	 * // Timestamp validation String env =
	 * Setting.getInstance().get(Setting.SYSTEM_ENV, "beta"); if
	 * (!env.equalsIgnoreCase("dev")) { if (request.getTimestamp() == null ||
	 * !Globalizer.validTimeStamp(request.getTimestamp())) { errors.put("timestamp",
	 * "Invalid timestamp."); } }
	 * 
	 * // Hibernate bean validation Validator validator =
	 * Validation.buildDefaultValidatorFactory().getValidator();
	 * Set<ConstraintViolation<I>> violoationSet = validator.validate(request); for
	 * (ConstraintViolation<I> v : violoationSet) { String propertyName =
	 * Globalizer.camelToLowerUnderScore(v.getPropertyPath().toString());
	 * errors.put(propertyName, v.getMessage()); }
	 * 
	 * // Validate DB input for (Field field : reqClass.getDeclaredFields()) {
	 * Column column = field.getAnnotation(Column.class); if (column != null) { //
	 * Need to develop DB validation here } }
	 * 
	 * return errors; }
     */
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return this.jsonMapper;
    }

}
