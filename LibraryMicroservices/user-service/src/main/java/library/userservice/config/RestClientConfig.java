package library.userservice.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Configuration
public class RestClientConfig {


    @Bean
    public RestClient bookServiceRestClient(LoadBalancerClient loadBalancerClient) {

        return RestClient.builder()
                .requestInterceptor((request, body, execution) -> {
                    URI originalUri = request.getURI();

                    if ("lb".equals(originalUri.getScheme())) {
                        String serviceName = originalUri.getHost();

                        ServiceInstance instance = loadBalancerClient.choose(serviceName);
                        if (instance != null) {
                            String newUrl = instance.getUri().toString() + originalUri.getPath() +
                                    (originalUri.getQuery() != null ? "?" + originalUri.getQuery() : "");

                            HttpRequest newRequest = new HttpRequestWrapper(request) {
                                @Override
                                public URI getURI() {
                                    return URI.create(newUrl);
                                }
                            };
                            return execution.execute(newRequest, body);
                        }
                    }
                    return execution.execute(request, body);
                })
                .build();
    }
}
