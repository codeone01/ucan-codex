package com.ucan.controller;

import com.ucan.service.AuthService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) { this.authService = authService; }

  record SignupReq(@NotBlank String fullName, @Email String email, @NotBlank String password) {}
  record LoginReq(@Email String email, @NotBlank String password) {}
  record VerifyReq(@NotBlank String token) {}

  @PostMapping("/signup")
  public void signup(@RequestBody SignupReq req) {
    authService.signup(req.fullName(), req.email(), req.password());
  }

  @PostMapping("/login")
  public Map<String, Object> login(@RequestBody LoginReq req) {
    return authService.login(req.email(), req.password());
  }

  @PostMapping("/verify-email")
  public void verify(@RequestBody VerifyReq req) {
    authService.verifyEmail(req.token());
  }

  @GetMapping("/me")
  public Map<String, Object> me(Principal principal) {
    return authService.me(principal.getName());
  }
}
