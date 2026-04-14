package com.ucan.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  private final SecretKey key;

  public JwtAuthFilter(@Value("${app.jwt.secret}") String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
    var auth = request.getHeader("Authorization");
    if (auth != null && auth.startsWith("Bearer ")) {
      var token = auth.substring(7);
      try {
        var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        var email = claims.getSubject();
        var role = String.valueOf(claims.get("role"));
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
        SecurityContextHolder.getContext().setAuthentication(
          new UsernamePasswordAuthenticationToken(email, null, authorities)
        );
      } catch (Exception ignored) { }
    }
    filterChain.doFilter(request, response);
  }
}
