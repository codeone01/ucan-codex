package com.ucan.controller;

import com.ucan.service.UserService;
import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users/me/dashboard")
  public Object dashboard(Principal principal) {
    return userService.dashboard(principal.getName());
  }

  @GetMapping("/admin/metrics")
  public Object metrics() {
    return userService.adminMetrics();
  }
}
