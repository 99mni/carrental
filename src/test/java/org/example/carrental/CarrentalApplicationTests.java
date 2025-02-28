package org.example.carrental;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CarrentalApplicationTests {

	@Test
	void contextLoads() {
		assertThat(1+1).isEqualTo(2);
	}

}
