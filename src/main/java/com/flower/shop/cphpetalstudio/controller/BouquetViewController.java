package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bouquets")
public class BouquetViewController {

    @Autowired
    private BouquetService bouquetService;

    @GetMapping
    public String viewBouquets(Model model) {
        List<Bouquet> bouquets = bouquetService.getAllBouquets();
        model.addAttribute("bouquets", bouquets);
        return "bouquets/list";
    }

    @GetMapping("/{id}")
    public String viewBouquet(@PathVariable Long id, Model model) {
        Bouquet bouquet = bouquetService.getBouquetById(id);
        model.addAttribute("bouquet", bouquet);
        return "bouquets/view";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String createBouquetForm(Model model) {
        model.addAttribute("bouquet", new Bouquet());
        return "bouquets/create";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createBouquet(@ModelAttribute Bouquet bouquet) {
        bouquetService.createBouquet(bouquet);
        return "redirect:/bouquets";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editBouquetForm(@PathVariable Long id, Model model) {
        Bouquet bouquet = bouquetService.getBouquetById(id);
        model.addAttribute("bouquet", bouquet);
        return "bouquets/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/edit")
    public String editBouquet(@PathVariable Long id, @ModelAttribute Bouquet bouquet) {
        bouquetService.updateBouquet(id, bouquet);
        return "redirect:/bouquets";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteBouquet(@PathVariable Long id) {
        bouquetService.deleteBouquet(id);
        return "redirect:/bouquets";
    }
}