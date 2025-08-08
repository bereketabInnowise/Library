package library.userservice.service;

//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import library.userservice.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Service
public class BookClientService {
    private final RestClient restClient;

    public BookClientService(@Qualifier("bookServiceRestClient") RestClient restClient) {
        System.out.println(">> RestClient injected: " + restClient.getClass());
        this.restClient = restClient;
    }

    @CircuitBreaker(name = "bookService", fallbackMethod = "fallbackBooks")
    public List<BookDTO> getBooksByUser(String username, String token) {
        return restClient.get()
                .uri("lb://book-service/api/v1/books")  // Full URL with lb://
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(List.class);
    }

    public List<BookDTO> fallbackBooks(String username, String token, Throwable t) {
        return Collections.emptyList();
    }
}