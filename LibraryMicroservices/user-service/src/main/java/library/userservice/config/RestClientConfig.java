package library.userservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Configuration
public class RestClientConfig {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Bean
    public RestClient bookServiceRestClient() {
        System.out.println(">> Creating RestClient with explicit LoadBalancer support");
        System.out.println(">> LoadBalancerClient: " + loadBalancerClient.getClass());

        return RestClient.builder()
                .requestInterceptor((request, body, execution) -> {
                    URI originalUri = request.getURI();
                    System.out.println(">> Intercepting request to: " + originalUri);

                    if ("lb".equals(originalUri.getScheme())) {
                        String serviceName = originalUri.getHost();
                        System.out.println(">> Resolving service: " + serviceName);

                        ServiceInstance instance = loadBalancerClient.choose(serviceName);
                        if (instance != null) {
                            String newUrl = instance.getUri().toString() + originalUri.getPath() +
                                    (originalUri.getQuery() != null ? "?" + originalUri.getQuery() : "");
                            System.out.println(">> Resolved to: " + newUrl);

                            HttpRequest newRequest = new HttpRequestWrapper(request) {
                                @Override
                                public URI getURI() {
                                    return URI.create(newUrl);
                                }
                            };
                            return execution.execute(newRequest, body);
                        } else {
                            System.out.println(">> No instance found for service: " + serviceName);
                        }
                    }
                    return execution.execute(request, body);
                })
                .build();
    }
}
