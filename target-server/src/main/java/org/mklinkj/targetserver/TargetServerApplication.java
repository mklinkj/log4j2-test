package org.mklinkj.targetserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @EnableWebMvc
// @ComponentScan(basePackages = "org.mklinkj")
@SpringBootApplication
public class TargetServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(TargetServerApplication.class, args);
  }
}
