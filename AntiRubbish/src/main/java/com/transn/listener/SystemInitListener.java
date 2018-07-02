package com.transn.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.util.WebUtils;

public class SystemInitListener extends ContextLoaderListener {

	private static final Logger logger = Logger
			.getLogger(SystemInitListener.class);

	/* 启动监听 */
	public void contextInitialized(ServletContextEvent event) {
		logger.info("init Starting.....");

		ServletContext sc = event.getServletContext();
		WebUtils.setWebAppRootSystemProperty(sc);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);

		logger.info("listener shutdown....");
	}
}
