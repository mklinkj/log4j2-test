package org.mklinkj.ldapserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LdapController {

  @GetMapping("/run")
  public String run() {
    return "Success";
  }
}
