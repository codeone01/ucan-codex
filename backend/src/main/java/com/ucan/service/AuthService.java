package com.ucan.service;

import com.ucan.security.JwtService;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private static final BigDecimal SIGNUP_BONUS = new BigDecimal("100000.00");
  private static final BigDecimal VERIFY_BONUS = new BigDecimal("100000000.00");

  private final JdbcTemplate jdbc;
  private final JwtService jwtService;
  private final EmailService emailService;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public AuthService(JdbcTemplate jdbc, JwtService jwtService, EmailService emailService) {
    this.jdbc = jdbc;
    this.jwtService = jwtService;
    this.emailService = emailService;
  }

  public void signup(String fullName, String email, String password) {
    var userId = UUID.randomUUID();
    var hash = encoder.encode(password);
    jdbc.update("insert into profiles(id, full_name, email, password_hash, email_verified) values (?,?,?,?,false)", userId, fullName, email, hash);
    jdbc.update("insert into user_roles(user_id, role) values (?, 'customer')", userId);
    jdbc.update("insert into user_balances(user_id, balance) values (?, ?)", userId, SIGNUP_BONUS);

    var token = UUID.randomUUID().toString();
    jdbc.update("insert into email_verification_tokens(user_id, token, expires_at, used) values (?,?,?,false)", userId, token, Instant.now().plusSeconds(86400));
    emailService.sendVerificationEmail(email, token);
  }

  public Map<String, Object> login(String email, String password) {
    var row = jdbc.queryForMap("select p.id, p.password_hash, coalesce(r.role,'customer') role from profiles p left join user_roles r on r.user_id = p.id where p.email=?", email);
    var valid = encoder.matches(password, String.valueOf(row.get("password_hash")));
    if (!valid) throw new RuntimeException("Invalid credentials");
    var role = String.valueOf(row.get("role"));
    var token = jwtService.generateToken(Map.of("role", role), email);
    return Map.of("token", token, "user", Map.of("email", email, "role", role));
  }

  public void verifyEmail(String token) {
    var rows = jdbc.queryForList("select t.user_id, t.used, p.email from email_verification_tokens t join profiles p on p.id=t.user_id where t.token=?", token);
    if (rows.isEmpty()) throw new RuntimeException("Invalid token");
    var data = rows.get(0);
    var userId = data.get("user_id");
    if (Boolean.TRUE.equals(data.get("used"))) throw new RuntimeException("Token already used");

    jdbc.update("update email_verification_tokens set used=true where token=?", token);
    jdbc.update("update profiles set email_verified=true where id=?", userId);

    Integer granted = jdbc.queryForObject("select count(*) from email_bonus_logs where user_id=? and bonus_type='email_verification'", Integer.class, userId);
    if (granted != null && granted == 0) {
      jdbc.update("update user_balances set balance = balance + ? where user_id=?", VERIFY_BONUS, userId);
      jdbc.update("insert into email_bonus_logs(user_id, bonus_type, amount) values (?, 'email_verification', ?)", userId, VERIFY_BONUS);
      emailService.sendBonusConfirmation(String.valueOf(data.get("email")), VERIFY_BONUS);
    }
  }

  public Map<String, Object> me(String email) {
    return jdbc.queryForMap("select p.full_name as \"fullName\", p.email, p.email_verified as \"emailVerified\", coalesce(r.role,'customer') role from profiles p left join user_roles r on r.user_id=p.id where p.email=?", email);
  }
}
