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

    // View all bouquets for both normal users and admins
    @GetMapping
    public String viewBouquets(Model model) {
        List<Bouquet> bouquets = bouquetService.getAllBouquets();
        model.addAttribute("bouquets", bouquets);
        return "bouquets/list";  // List view for all users
    }

    // View bouquet details for both normal users and admins
    @GetMapping("/{id}")
    public String viewBouquet(@PathVariable Long id, Model model) {
        Bouquet bouquet = bouquetService.getBouquetById(id);
        model.addAttribute("bouquet", bouquet);
        return "bouquets/view";  // Single bouquet view
    }

    // Admin only: Form to create a new bouquet
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String createBouquetForm(Model model) {
        model.addAttribute("bouquet", new Bouquet());
        return "bouquets/create";  // Create form for admin
    }

    // Admin only: Handling POST request for creating new bouquet
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createBouquet(@ModelAttribute Bouquet bouquet) {
        bouquetService.createBouquet(bouquet);
        return "redirect:/bouquets";  // Redirect back to the bouquet list
    }

    // Admin only: Edit form for an existing bouquet
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editBouquetForm(@PathVariable Long id, Model model) {
        Bouquet bouquet = bouquetService.getBouquetById(id);
        model.addAttribute("bouquet", bouquet);
        return "bouquets/edit";  // Edit form for admin
    }

    // Admin only: Handle the POST request for updating bouquet
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/edit")
    public String editBouquet(@PathVariable Long id, @ModelAttribute Bouquet bouquet) {
        bouquetService.updateBouquet(id, bouquet);
        return "redirect:/bouquets";  // Redirect back to the bouquet list
    }

    // Admin only: Delete bouquet
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteBouquet(@PathVariable Long id) {
        bouquetService.deleteBouquet(id);
        return "redirect:/bouquets";  // Redirect back to the bouquet list
    }
}
