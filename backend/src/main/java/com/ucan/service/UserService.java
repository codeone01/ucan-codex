package com.ucan.service;

import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final JdbcTemplate jdbc;

  public UserService(JdbcTemplate jdbc) { this.jdbc = jdbc; }

  public Map<String, Object> dashboard(String email) {
    var profile = jdbc.queryForMap("select p.id, p.full_name as \"fullName\", ub.balance from profiles p join user_balances ub on ub.user_id=p.id where p.email=?", email);
    var userId = profile.get("id");
    return Map.of(
      "profile", profile,
      "orders", jdbc.queryForList("select id, order_code, status, total_amount as \"totalAmount\" from orders where user_id=? order by created_at desc", userId),
      "cards", jdbc.queryForList("select id, nickname, right(card_number,4) as last4 from fake_cards where user_id=?", userId),
      "addresses", jdbc.queryForList("select id, address_line as \"addressLine\", city from saved_addresses where user_id=? order by created_at desc", userId),
      "favorites", jdbc.queryForList("select f.product_id as \"productId\", p.name as \"productName\" from favorites f join products p on p.id=f.product_id where f.user_id=?", userId)
    );
  }

  public Map<String, Object> adminMetrics() {
    return Map.of(
      "fictionalRevenue", jdbc.queryForObject("select coalesce(sum(total_amount),0) from orders", Double.class),
      "totalOrders", jdbc.queryForObject("select count(*) from orders", Integer.class),
      "totalUsers", jdbc.queryForObject("select count(*) from profiles", Integer.class)
    );
  }
}
