package org.mklinkj.ldapserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "org.mklinkj.ldapserver")
@SpringBootApplication
public class LdapServerApplication implements CommandLineRunner {
  @Autowired private LdapServer ldapServer;

  public static void main(String[] args) {
    SpringApplication.run(LdapServerApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    ldapServer.start();
  }
}
