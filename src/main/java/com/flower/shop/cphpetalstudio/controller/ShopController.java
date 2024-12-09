package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.Order;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import com.flower.shop.cphpetalstudio.service.OrderService;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private BouquetService bouquetService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String viewShop(Model model,
                           @RequestParam(required = false) BigDecimal maxPrice,
                           @RequestParam(required = false) BigDecimal minPrice,
                           @RequestParam(required = false) String category) {
        List<Bouquet> bouquets;

        if (maxPrice != null) {
            bouquets = bouquetService.getBouquetsUnderPrice(maxPrice);
        } else if (minPrice != null) {
            bouquets = bouquetService.getBouquetsOverPrice(minPrice);
        } else if (category != null) {
            bouquets = bouquetService.getBouquetsByCategory(category);
        } else {
            bouquets = bouquetService.getAllBouquets();
        }

        model.addAttribute("bouquets", bouquets);
        return "shop/list";
    }

    @GetMapping("/{id}")
    public String viewBouquet(@PathVariable Long id, Model model) {
        model.addAttribute("bouquet", bouquetService.getBouquetById(id));
        return "shop/view";
    }

    @PostMapping("/order")
    public String createOrder(@RequestParam List<Long> bouquetIds, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<Bouquet> bouquets = bouquetService.getBouquetsByIds(bouquetIds);
        Order order = orderService.createOrder(user, bouquets);
        return "redirect:/shop/order/" + order.getId();
    }

    @GetMapping("/order/{id}")
    public String viewOrder(@PathVariable Long id, Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Order order = orderService.getOrderById(id);
        if (!order.getUser().equals(user)) {
            return "redirect:/shop";
        }
        model.addAttribute("order", order);
        return "shop/order";
    }

    @GetMapping("/search")
    public String searchBouquets(@RequestParam String query, Model model) {
        List<Bouquet> bouquets = bouquetService.searchBouquets(query);
        model.addAttribute("bouquets", bouquets);
        return "shop/list";
    }
}