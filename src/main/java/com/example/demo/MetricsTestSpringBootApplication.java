package com.example.demo;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

@SpringBootApplication
public class MetricsTestSpringBootApplication {

    public static Random random = new Random();

    public static void request(Meter meter) {
        System.out.println("request");
        meter.mark();
    }

    public static void request(Meter meter, int n) {
        while (n > 0) {
            request(meter);
            n--;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MetricsTestSpringBootApplication.class, args);

        MetricRegistry registry = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(1, TimeUnit.SECONDS);
        Meter meterTps = registry.meter(
                MetricRegistry.name(MetricsTestSpringBootApplication.class, "request", "tps"));
        while (true) {
            request(meterTps, random.nextInt(5));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
