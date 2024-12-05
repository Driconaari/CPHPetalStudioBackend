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
public class BouquetController {

    @Autowired
    private BouquetService bouquetService;

    // View all bouquets for both normal users and admins
    @GetMapping
    public String viewBouquets(Model model) {
        List<Bouquet> bouquets = bouquetService.getAllBouquets();
        model.addAttribute("bouquets", bouquets);
        return "bouquet/list"; // This will render 'list.html' using Thymeleaf
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

    // REST API: GET all bouquets (accessible by both normal users and admins)
    @GetMapping("/api")
    @ResponseBody
    public List<Bouquet> getAllBouquets() {
        return bouquetService.getAllBouquets();
    }

    // REST API: GET bouquet by ID (accessible by both normal users and admins)
    @GetMapping("/api/{id}")
    @ResponseBody
    public Bouquet getBouquetById(@PathVariable Long id) {
        return bouquetService.getBouquetById(id);
    }

    // REST API: Search bouquets (accessible by both normal users and admins)
    @GetMapping("/api/search")
    @ResponseBody
    public List<Bouquet> searchBouquets(@RequestParam String name) {
        return bouquetService.searchBouquets(name);
    }

    // REST API: Get bouquets by category (accessible by both normal users and admins)
    @GetMapping("/api/category/{category}")
    @ResponseBody
    public List<Bouquet> getBouquetsByCategory(@PathVariable String category) {
        return bouquetService.getBouquetsByCategory(category);
    }

    // REST API: Get latest bouquets (accessible by both normal users and admins)
    @GetMapping("/api/latest")
    @ResponseBody
    public List<Bouquet> getLatestBouquets(@RequestParam(defaultValue = "5") int limit) {
        return bouquetService.getLatestBouquets(limit);
    }

    // REST API: Admin only: Create a new bouquet
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api")
    @ResponseBody
    public Bouquet createBouquetApi(@RequestBody Bouquet bouquet) {
        return bouquetService.createBouquet(bouquet);
    }

    // REST API: Admin only: Update a bouquet
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/{id}")
    @ResponseBody
    public Bouquet updateBouquetApi(@PathVariable Long id, @RequestBody Bouquet bouquetDetails) {
        return bouquetService.updateBouquet(id, bouquetDetails);
    }

    // REST API: Admin only: Delete a bouquet
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public void deleteBouquetApi(@PathVariable Long id) {
        bouquetService.deleteBouquet(id);
    }
}