package net.xjulio.promethesexporterdemo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.distribution.ValueAtPercentile;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@SpringBootTest
class PromethesExporterDemoApplicationTests {

	@Test
	void contextLoads() {
		SimpleMeterRegistry registry = new SimpleMeterRegistry();
		Timer timer = Timer
		  .builder("test.timer")
		  .publishPercentiles(0.3, 0.5, 0.95)
		  .publishPercentileHistogram()
		  .register(registry);
		
		timer.record(2, TimeUnit.SECONDS);
		timer.record(2, TimeUnit.SECONDS);
		timer.record(3, TimeUnit.SECONDS);
		timer.record(4, TimeUnit.SECONDS);
		timer.record(8, TimeUnit.SECONDS);
		timer.record(13, TimeUnit.SECONDS);
		
		Map<Double, Double> actualMicrometer = new TreeMap<>();
		ValueAtPercentile[] percentiles = timer.takeSnapshot().percentileValues();
		for (ValueAtPercentile percentile : percentiles) {
		    actualMicrometer.put(percentile.percentile(), percentile.value(TimeUnit.MILLISECONDS));
		}

		Map<Double, Double> expectedMicrometer = new TreeMap<>();
		expectedMicrometer.put(0.3, 1946.157056);
		expectedMicrometer.put(0.5, 3019.89888);
		expectedMicrometer.put(0.95, 13354.663936);

		assertEquals(expectedMicrometer, actualMicrometer);
	}

}

