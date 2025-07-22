package com.sprint.mission.discodeit.mdc;

import io.micrometer.context.ThreadLocalAccessor;
import java.util.Map;
import org.slf4j.MDC;

public class MdcThreadLocalAccessor implements ThreadLocalAccessor<Map<String, String>> {

  @Override
  public Object key() {
    return MdcThreadLocalAccessor.class.getName();
  }

  @Override
  public Map<String, String> getValue() {
    return MDC.getCopyOfContextMap();
  }

  @Override
  public void setValue(Map<String, String> contextMap) {
    if (contextMap == null) {
      MDC.clear();
    } else {
      MDC.setContextMap(contextMap);
    }
  }

  @Override
  public void setValue() {
    MDC.clear();
  }

  @Override
  public void restore(Map<String, String> previous) {
    setValue(previous);
  }
}
