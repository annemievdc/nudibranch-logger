package be.thomasmore.nudibranch.controllers;

import be.thomasmore.nudibranch.model.Diet;
import be.thomasmore.nudibranch.repositories.DietRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class DietController {

    private final DietRepository dietRepository;

    public DietController(DietRepository dietRepository) {
        this.dietRepository = dietRepository;
    }

    private static final String DIET_IMG_PATH = "/img/diets/";


    @GetMapping({"/diet-details/{dietId}", "/diet-details"})
    public String dietDetails(Model model, @PathVariable(required = false) Integer dietId) {
        if (dietId == null) {
            dietId = 1;
        }
        Optional<Diet> dietFromDb = dietRepository.findById(dietId);
        long count = dietRepository.count();
        if(dietFromDb.isPresent()){
            Diet diet = dietFromDb.get();
            diet.setDietImageUrl(DIET_IMG_PATH + diet.getDietImageUrl());
            model.addAttribute("diet", diet);
        }
        long prevId =(dietId >1) ? dietId - 1 : count;
        long nextId = (dietId <count)? dietId + 1 : 1;
        model.addAttribute("prevId", prevId);
        model.addAttribute("nextId", nextId);
        model.addAttribute("pageTitle","Dieet Details");


        return "diet-details";
    }

    @GetMapping("/diet-overview")
    public String dietOverview(Model model,
                               @RequestParam(required = false) String dietCommonName,
                               @RequestParam(required = false) String dietScientificName) {

        // Filter diets
        List<Diet> diets = dietRepository.findByDietFilter(dietCommonName, dietScientificName);

        // Set image paths
        diets.forEach(diet -> diet.setDietImageUrl(DIET_IMG_PATH + diet.getDietImageUrl()));

        // Group by category name
        Map<String, List<Diet>> dietsByCategory = diets.stream()
                .collect(Collectors.groupingBy(d -> d.getDietCategory() != null ? d.getDietCategory().getCategoryName() : "Geen categorie"));

        // Add attributes for Thymeleaf
        model.addAttribute("diets", diets);
        model.addAttribute("dietsByCategory", dietsByCategory);
        model.addAttribute("pageTitle","Dieet Overzicht");

        return "diet-overview";
    }

}