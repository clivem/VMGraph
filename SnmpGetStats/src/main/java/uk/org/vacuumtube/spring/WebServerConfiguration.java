/**
 * 
 */
package uk.org.vacuumtube.spring;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerServiceExporter;
import org.springframework.remoting.support.SimpleHttpServerFactoryBean;

import uk.org.vacuumtube.http.CustomHttpInvokerServiceExporter;
import uk.org.vacuumtube.http.EchoHandler;
import uk.org.vacuumtube.http.JBossHttpInvokerServiceExporter;
import uk.org.vacuumtube.http.SimpleHttpsServerFactoryBean;
import uk.org.vacuumtube.service.ServiceLocator;
import uk.org.vacuumtube.service.StatsDatabaseService;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;

/**
 * @author clivem
 *
 */
@Configuration
@Import(ApplicationConfiguration.class)
public class WebServerConfiguration {

	private final String keystoreLocation = "classpath:META-INF/certs/vacuumtube.keystore";
	private final String keystorePassword = "vacuumtube";
	
	@Autowired
	private ApplicationConfiguration applicationConfiguration;
	
	@Bean
	public SimpleHttpInvokerServiceExporter statsDatabaseServiceHttpExporter() {
		SimpleHttpInvokerServiceExporter exporter = new SimpleHttpInvokerServiceExporter();
		exporter.setService(applicationConfiguration.statsDatabaseService());
		exporter.setServiceInterface(StatsDatabaseService.class);
		return exporter;
	}

	@Bean
	public SimpleHttpInvokerServiceExporter customStatsDatabaseServiceHttpExporter() {
		SimpleHttpInvokerServiceExporter exporter = new CustomHttpInvokerServiceExporter();
		exporter.setService(applicationConfiguration.statsDatabaseService());
		exporter.setServiceInterface(StatsDatabaseService.class);
		return exporter;
	}

	@Bean
	public SimpleHttpInvokerServiceExporter jbossStatsDatabaseServiceHttpExporter() {
		SimpleHttpInvokerServiceExporter exporter = new JBossHttpInvokerServiceExporter();
		exporter.setService(applicationConfiguration.statsDatabaseService());
		exporter.setServiceInterface(StatsDatabaseService.class);
		return exporter;
	}
	
	/*
	@Bean(name = "statsDatabaseServiceBurlapExporter")
	public SimpleBurlapServiceExporter statsDatabaseServiceBurlapExporter() {
		SimpleBurlapServiceExporter exporter = new SimpleBurlapServiceExporter();
		exporter.setService(applicationConfiguration.statsDatabaseService());
		exporter.setServiceInterface(StatsDatabaseService.class);
		return exporter;
	}
	
	@Bean(name = "statsDatabaseServiceHessianExporter")
	public SimpleHessianServiceExporter statsDatabaseServiceHessianExporter() {
		SimpleHessianServiceExporter exporter = new SimpleHessianServiceExporter();
		exporter.setService(applicationConfiguration.statsDatabaseService());
		exporter.setServiceInterface(StatsDatabaseService.class);
		return exporter;
	}
	*/
	
	@Bean
	public SSLContext defaultSSLContext() throws Exception {
		SSLContext sslContext = SSLContext.getInstance("TLS");
        char[] password = keystorePassword.toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        //FileInputStream fis = new FileInputStream(System.getProperty("javax.net.ssl.keyStore"));
        InputStream is = new DefaultResourceLoader().getResource(keystoreLocation).getInputStream();
        ks.load(is, password);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        return sslContext;
	}
	
	@Bean
	public HttpsConfigurator defaultHttpsConfigurator() throws Exception {
        HttpsConfigurator httpsConfigurator = new HttpsConfigurator(defaultSSLContext()) {
			@Override
			public void configure(HttpsParameters httpsParameters) {
				SSLContext sslContext = getSSLContext();
				SSLParameters defaultSSLParameters = 
						sslContext.getDefaultSSLParameters();
				defaultSSLParameters.setNeedClientAuth(false);
				httpsParameters.setSSLParameters(defaultSSLParameters);
			}
		};
		return httpsConfigurator;
	}
	
	@Bean(name = "httpsServerFactory")
	public SimpleHttpsServerFactoryBean simpleHttpsServerFactory() throws Exception {
		Map<String, HttpHandler> map = new HashMap<String, HttpHandler>();
		map.put(ServiceLocator.WEB_CONTEXT_PATH + ServiceLocator.STATS_SERVICE_HTTP_SIMPLE_JAVA_SERVICE_NAME, 
				statsDatabaseServiceHttpExporter());
		map.put(ServiceLocator.WEB_CONTEXT_PATH + ServiceLocator.STATS_SERVICE_HTTP_COMMONS_JAVA_SERVICE_NAME, 
				customStatsDatabaseServiceHttpExporter());
		map.put(ServiceLocator.WEB_CONTEXT_PATH + ServiceLocator.STATS_SERVICE_HTTP_COMMONS_JBOSS_SERVICE_NAME, 
				jbossStatsDatabaseServiceHttpExporter());
		//map.put("/remoting/BurlapStatsDatabaseService", statsDatabaseServiceBurlapExporter());
		//map.put("/remoting/HessianStatsDatabaseService", statsDatabaseServiceHessianExporter());
		map.put("/", new EchoHandler());

		SimpleHttpsServerFactoryBean httpsServerFactory = new SimpleHttpsServerFactoryBean();
		httpsServerFactory.setHttpsConfigurator(defaultHttpsConfigurator());
		httpsServerFactory.setContexts(map);
		//httpServerFactory.setAuthenticator(new RealmAuthenticator("Vacuumtube"));
		//httpServerFactory.setHostname("127.0.0.1");
		httpsServerFactory.setPort(4443);
		httpsServerFactory.setExecutor(new ThreadPoolExecutor(4, 8, 0, TimeUnit.MILLISECONDS, 
				new ArrayBlockingQueue<Runnable>(4)));
		return httpsServerFactory;
	}
	
	@Bean(name = "httpServerFactory")
	public SimpleHttpServerFactoryBean simpleHttpServerFactory() {
		Map<String, HttpHandler> map = new HashMap<String, HttpHandler>();
		map.put(ServiceLocator.WEB_CONTEXT_PATH + ServiceLocator.STATS_SERVICE_HTTP_SIMPLE_JAVA_SERVICE_NAME, 
				statsDatabaseServiceHttpExporter());
		map.put(ServiceLocator.WEB_CONTEXT_PATH + ServiceLocator.STATS_SERVICE_HTTP_COMMONS_JAVA_SERVICE_NAME, 
				customStatsDatabaseServiceHttpExporter());
		map.put(ServiceLocator.WEB_CONTEXT_PATH + ServiceLocator.STATS_SERVICE_HTTP_COMMONS_JBOSS_SERVICE_NAME, 
				jbossStatsDatabaseServiceHttpExporter());
		//map.put("/remoting/BurlapStatsDatabaseService", statsDatabaseServiceBurlapExporter());
		//map.put("/remoting/HessianStatsDatabaseService", statsDatabaseServiceHessianExporter());
		map.put("/", new EchoHandler());
		
		SimpleHttpServerFactoryBean httpServerFactory = new SimpleHttpServerFactoryBean();		
		httpServerFactory.setContexts(map);
		//httpServerFactory.setAuthenticator(new RealmAuthenticator("Vacuumtube"));
		//httpServerFactory.setHostname("127.0.0.1");
		httpServerFactory.setPort(8080);
		return httpServerFactory;
	}
}
