package com.hubspot.rosetta;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Build a {@link RosettaMapper}.
 *
 * @author tdavis
 */
public class RosettaMapperFactory {

  public static <T> RosettaMapper<T> forType(Class<T> type) {
      return new RosettaMapper<T>(type, Rosetta.getMapper());
  }

  @SuppressWarnings("unchecked")
  public static <T> RosettaMapper<T> forType(TypeReference<T> ref) {
    ObjectMapper mapper = Rosetta.getMapper();
    return new RosettaMapper<T>((Class<T>)mapper.getTypeFactory().constructType(ref).getRawClass(), mapper);
  }


}
