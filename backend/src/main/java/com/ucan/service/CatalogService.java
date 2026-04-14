package com.ucan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {
  private final JdbcTemplate jdbc;

  public CatalogService(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<Map<String, Object>> products(Map<String, String> f) {
    var sql = new StringBuilder("""
      select p.id, p.name, p.short_description as \"shortDescription\", p.price, p.featured
      from products p
      join categories c on c.id=p.category_id
      join subcategories s on s.id=p.subcategory_id
      join brands b on b.id=p.brand_id
      join product_types t on t.id=p.type_id
      join colors co on co.id=p.color_id
      where 1=1
      """);
    var args = new java.util.ArrayList<>();
    if (f.get("q") != null && !f.get("q").isBlank()) {
      sql.append(" and lower(p.name) like lower(?)");
      args.add("%" + f.get("q") + "%");
    }
    appendFilter(sql, args, "category", "c.slug", f.get("category"));
    appendFilter(sql, args, "subcategory", "s.slug", f.get("subcategory"));
    appendFilter(sql, args, "brand", "b.slug", f.get("brand"));
    appendFilter(sql, args, "type", "t.slug", f.get("type"));
    appendFilter(sql, args, "color", "co.slug", f.get("color"));

    var sort = f.getOrDefault("sort", "featured");
    sql.append(switch (sort) {
      case "price_asc" -> " order by p.price asc";
      case "price_desc" -> " order by p.price desc";
      case "newest" -> " order by p.created_at desc";
      default -> " order by p.featured desc, p.created_at desc";
    });
    return jdbc.queryForList(sql.toString(), args.toArray());
  }

  private void appendFilter(StringBuilder sql, java.util.List<Object> args, String key, String column, String value) {
    if (value != null && !value.isBlank()) {
      sql.append(" and ").append(column).append("=?");
      args.add(value);
    }
  }

  public Map<String, Object> metadata() {
    Map<String, Object> result = new HashMap<>();
    result.put("categories", jdbc.queryForList("select name, slug from categories order by name"));
    result.put("subcategories", jdbc.queryForList("select s.name, s.slug, c.slug as category_slug as \"categorySlug\" from subcategories s join categories c on c.id=s.category_id order by s.name"));
    result.put("brands", jdbc.queryForList("select name, slug from brands order by name"));
    result.put("types", jdbc.queryForList("select name, slug from product_types order by name"));
    result.put("colors", jdbc.queryForList("select name, slug from colors order by name"));
    return result;
  }
}
