package com.pivotal.cf.broker.services;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public interface TemplateService {

	public abstract boolean execute(String templateName, Map<String, Object> model);

	public abstract void setTemplate(JdbcTemplate template);

}