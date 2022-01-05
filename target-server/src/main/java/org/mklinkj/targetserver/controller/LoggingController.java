package org.mklinkj.targetserver.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Log4j2
@Controller
public class LoggingController {
  @GetMapping(value = {"/", "/index"})
  public String index(Model model) {
    model.addAttribute("serverTime", LocalDateTime.now());
    return "index";
  }

  @PostMapping("/form")
  public String form(String ldapString, RedirectAttributes rttr) {
    try {
      LOGGER.info("{}", ldapString);
      rttr.addFlashAttribute("exception", "예외 발생 X");
    } catch (Exception e) {
      rttr.addFlashAttribute("exception", "예외발생 O: " + e.getMessage());
    }
    return "redirect:/";
  }
}
