package library.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/debug/versions")
    public Map<String, Object> checkVersions() {
        Map<String, Object> result = new HashMap<>();

        // Check Spring Boot version
        result.put("Spring Boot Version", SpringBootVersion.getVersion());

        // Check for LoadBalancer classes
        try {
            Class.forName("org.springframework.cloud.client.loadbalancer.LoadBalancerClient");
            result.put("LoadBalancerClient class", "Found");
        } catch (ClassNotFoundException e) {
            result.put("LoadBalancerClient class", "NOT FOUND");
        }

        try {
            Class.forName("org.springframework.cloud.loadbalancer.annotation.LoadBalanced");
            result.put("@LoadBalanced annotation", "Found");
        } catch (ClassNotFoundException e) {
            result.put("@LoadBalanced annotation", "NOT FOUND");
        }

        // Check for auto-configuration classes
        try {
            Class.forName("org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration");
            result.put("LoadBalancerAutoConfiguration", "Found");
        } catch (ClassNotFoundException e) {
            result.put("LoadBalancerAutoConfiguration", "NOT FOUND");
        }

        return result;
    }

    @GetMapping("/debug/beans")
    public Map<String, Object> checkBeans() {
        Map<String, Object> result = new HashMap<>();

        try {
            // Check for LoadBalancer infrastructure beans
            String[] lbClient = applicationContext.getBeanNamesForType(
                    Class.forName("org.springframework.cloud.client.loadbalancer.LoadBalancerClient"));
            result.put("LoadBalancerClient beans", Arrays.asList(lbClient));

            String[] discoveryBeans = applicationContext.getBeanNamesForType(
                    Class.forName("org.springframework.cloud.client.discovery.DiscoveryClient"));
            result.put("DiscoveryClient beans", Arrays.asList(discoveryBeans));

        } catch (Exception e) {
            result.put("error", e.getMessage());
        }

        return result;
    }
}