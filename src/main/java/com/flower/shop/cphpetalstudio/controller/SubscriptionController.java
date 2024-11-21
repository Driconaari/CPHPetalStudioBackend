package com.flower.shop.cphpetalstudio.controller;


import com.flower.shop.cphpetalstudio.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/subscriptions")
@PreAuthorize("hasRole('ADMIN')")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public String listSubscriptions(Model model) {
        model.addAttribute("subscriptions", subscriptionService.getAllActiveSubscriptions());
        return "admin/subscriptions/list";
    }

    @GetMapping("/{id}")
    public String viewSubscription(@PathVariable Long id, Model model) {
        model.addAttribute("subscription", subscriptionService.getSubscriptionById(id));
        return "admin/subscriptions/view";
    }