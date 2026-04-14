package com.ucan.controller;

import com.ucan.service.CatalogService;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {
  private final CatalogService catalogService;

  public CatalogController(CatalogService catalogService) { this.catalogService = catalogService; }

  @GetMapping("/products")
  public Object products(@RequestParam Map<String, String> filters) {
    return catalogService.products(filters);
  }

  @GetMapping("/metadata")
  public Object metadata() {
    return catalogService.metadata();
  }
}
