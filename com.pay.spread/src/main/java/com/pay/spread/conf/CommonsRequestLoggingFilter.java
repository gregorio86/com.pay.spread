package com.pay.spread.conf;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

/**
 * 해당 기능을 사용하기 위해선 log level 이 debug 로 설정이 되어 있어야 한다.
 * 
 */
public class CommonsRequestLoggingFilter extends AbstractRequestLoggingFilter {

	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
//		CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
//		loggingFilter.setIncludeHeaders(true);
//		loggingFilter.setIncludeQueryString(true);
//		loggingFilter.setIncludePayload(true);
//		loggingFilter.setMaxPayloadLength(100);
//		loggingFilter.setIncludeClientInfo(true);
//		loggingFilter.setBeforeMessagePrefix("Before : ");
//		loggingFilter.setBeforeMessageSuffix("");
//		loggingFilter.setAfterMessagePrefix("After : ");
//		loggingFilter.setAfterMessageSuffix("");
//		return loggingFilter;
		return new CommonsRequestLoggingFilter();
	}

	@Override
	protected boolean shouldLog(HttpServletRequest request) {
		return logger.isDebugEnabled();
	}

	@Override
	protected void beforeRequest(HttpServletRequest request, String message) {
		logger.debug(message);
	}

	@Override
	protected void afterRequest(HttpServletRequest request, String message) {
		logger.debug(message);
	}
}