package com.ucan.controller;

import com.ucan.service.CheckoutService;
import java.security.Principal;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
  private final CheckoutService checkoutService;

  public CheckoutController(CheckoutService checkoutService) { this.checkoutService = checkoutService; }

  @GetMapping("/cards")
  public Object cards(Principal principal) {
    return checkoutService.cards(principal.getName());
  }

  @PostMapping("/orders")
  public Map<String, Object> order(Principal principal, @RequestBody Map<String, Object> payload) {
    return checkoutService.createOrder(principal.getName(), payload);
  }
}
