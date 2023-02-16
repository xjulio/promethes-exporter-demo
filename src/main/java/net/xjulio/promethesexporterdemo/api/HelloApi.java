package net.xjulio.promethesexporterdemo.api;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
public class HelloApi {
	private static final Logger log = LoggerFactory.getLogger(HelloApi.class);
	
	private Counter testCounter;

	public HelloApi(MeterRegistry meterRegistry) {
		this.testCounter = Counter.builder("hello_requests_count").description("/hello requests counter")
				.register(meterRegistry);
	}

	@GetMapping("/hello")
	public String hello() {
		this.testCounter.increment();
		return "Hello Work";
	}
}
//
//# 1    5
//# x    1