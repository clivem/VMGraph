/**
 * 
 */
package uk.org.vacuumtube.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import uk.org.vacuumtube.service.StatsDatabaseService;

/**
 * @author clivem
 *
 */
@Configuration
public class WebClientConfiguration {

	@Bean(name = "statsHttpProxyFactory")
	public HttpInvokerProxyFactoryBean statsHttpProxyFactory() {
		HttpInvokerProxyFactoryBean invoker = new HttpInvokerProxyFactoryBean();
		invoker.setServiceUrl("http://127.0.0.1:8080/remoting/HttpStatsDatabaseService");
		invoker.setHttpInvokerRequestExecutor(new CommonsHttpInvokerRequestExecutor());
		invoker.setServiceInterface(StatsDatabaseService.class);
		return invoker;
	}
	/*
	@Bean(name = "statsBurlapProxyFactory")
	public BurlapProxyFactoryBean statsBurlapProxyFactory() {
		BurlapProxyFactoryBean invoker = new BurlapProxyFactoryBean();
		invoker.setServiceUrl("http://127.0.0.1:8080/remoting/BurlapStatsDatabaseService");
		//invoker.setHttpInvokerRequestExecutor(new CommonsHttpInvokerRequestExecutor());
		invoker.setServiceInterface(StatsDatabaseService.class);
		return invoker;
	}

	@Bean(name = "statsHessianProxyFactory")
	public HessianProxyFactoryBean statsHessianProxyFactory() {
		HessianProxyFactoryBean invoker = new HessianProxyFactoryBean();
		invoker.setServiceUrl("http://127.0.0.1:8080/remoting/HessianStatsDatabaseService");
		//invoker.setHttpInvokerRequestExecutor(new CommonsHttpInvokerRequestExecutor());
		invoker.setServiceInterface(StatsDatabaseService.class);
		return invoker;
	}
	*/
}
