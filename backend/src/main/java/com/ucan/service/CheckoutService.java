package com.ucan.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckoutService {
  private final JdbcTemplate jdbc;
  private final EmailService emailService;

  public CheckoutService(JdbcTemplate jdbc, EmailService emailService) {
    this.jdbc = jdbc;
    this.emailService = emailService;
  }

  public List<Map<String, Object>> cards(String email) {
    return jdbc.queryForList("select fc.id, fc.nickname, right(fc.card_number,4) as last4 from fake_cards fc join profiles p on p.id=fc.user_id where p.email=?", email);
  }

  @Transactional
  public Map<String, Object> createOrder(String email, Map<String, Object> payload) {
    var user = jdbc.queryForMap("select id, email from profiles where email=?", email);
    var userId = user.get("id");

    Map<String, Object> card = (Map<String, Object>) payload.get("card");
    Object cardId = card.get("cardId");
    if (cardId == null) {
      jdbc.update("insert into fake_cards(user_id, printed_name, card_number, expiration, cvv, nickname, brand) values (?,?,?,?,?,?,?)",
        userId, card.get("cardPrintedName"), card.get("cardNumber"), card.get("expiration"), card.get("cvv"), card.get("nickname"), card.get("brand"));
    }

    var orderId = UUID.randomUUID();
    var code = "UC" + orderId.toString().substring(0, 8).toUpperCase();
    var items = (List<Map<String, Object>>) payload.get("items");
    BigDecimal total = items.stream().map(i -> new BigDecimal(String.valueOf(i.get("price"))).multiply(new BigDecimal(String.valueOf(i.get("qty"))))).reduce(BigDecimal.ZERO, BigDecimal::add);

    jdbc.update("insert into orders(id, user_id, order_code, status, total_amount, payment_method) values (?,?,?,?,?,?)", orderId, userId, code, "confirmed", total, payload.get("paymentMethod"));
    for (var i : items) {
      jdbc.update("insert into order_items(order_id, product_id, quantity, unit_price) values (?,?,?,?)", orderId, i.get("id"), i.get("qty"), i.get("price"));
    }

    Map<String, Object> shipping = (Map<String, Object>) payload.get("shipping");
    jdbc.update("insert into saved_addresses(user_id, address_line, city, state, zip_code, country) values (?,?,?,?,?,?)", userId, shipping.get("addressLine"), shipping.get("city"), shipping.get("state"), shipping.get("zipCode"), shipping.get("country"));
    jdbc.update("insert into order_status_history(order_id, status, note) values (?,?,?)", orderId, "confirmed", "Fictional payment approved");

    jdbc.update("update user_balances set balance = balance - ? where user_id=?", total, userId);
    emailService.sendOrderConfirmation(String.valueOf(user.get("email")), code, total);
    return Map.of("orderId", orderId, "orderCode", code);
  }
}
