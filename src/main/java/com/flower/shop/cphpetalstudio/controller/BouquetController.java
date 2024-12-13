package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/bouquets")
@PreAuthorize("hasRole('ADMIN')")
public class BouquetController {

    @Autowired
    private BouquetService bouquetService;

    @GetMapping
    public String listBouquets(Model model) {
        model.addAttribute("bouquets", bouquetService.getAllBouquets());
        return "admin/bouquets/list";
    }

    @GetMapping("/{id}")
    public String viewBouquet(@PathVariable Long id, Model model) {
        model.addAttribute("bouquet", bouquetService.getBouquetById(id));
        return "admin/bouquets/view";
    }

    @GetMapping("/create")
    public String createBouquetForm(Model model) {
        model.addAttribute("bouquet", new Bouquet());
        return "admin/bouquets/create";
    }

    @PostMapping("/create")
    public String createBouquet(@ModelAttribute Bouquet bouquet) {
        bouquetService.createBouquet(bouquet);
        return "redirect:/admin/bouquets";
    }

    @GetMapping("/{id}/edit")
    public String editBouquetForm(@PathVariable Long id, Model model) {
        model.addAttribute("bouquet", bouquetService.getBouquetById(id));
        return "admin/bouquets/edit";
    }

    @PostMapping("/{id}/edit")
    public String editBouquet(@PathVariable Long id, @ModelAttribute Bouquet bouquet) {
        bouquetService.updateBouquet(id, bouquet);
        return "redirect:/admin/bouquets";
    }

    @PostMapping("/{id}/delete")
    public String deleteBouquet(@PathVariable Long id) {
        bouquetService.deleteBouquet(id);
        return "redirect:/admin/bouquets";
    }
}