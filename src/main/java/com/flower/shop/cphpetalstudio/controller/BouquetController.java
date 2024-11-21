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

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("bouquet", new Bouquet());
        return "admin/bouquets/form";
    }

    @PostMapping("/add")
    public String addBouquet(@ModelAttribute Bouquet bouquet) {
        bouquetService.saveBouquet(bouquet);
        return "redirect:/admin/bouquets";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("bouquet", bouquetService.getBouquetById(id));
        return "admin/bouquets/form";
    }

    @PostMapping("/edit/{id}")
    public String updateBouquet(@PathVariable Long id, @ModelAttribute Bouquet bouquet) {
        bouquet.setId(id);
        bouquetService.saveBouquet(bouquet);
        return "redirect:/admin/bouquets";
    }

    @GetMapping("/delete/{id}")
    public String deleteBouquet(@PathVariable Long id) {
        bouquetService.deleteBouquet(id);
        return "redirect:/admin/bouquets";
    }
}