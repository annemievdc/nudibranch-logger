package be.thomasmore.nudibranch.controllers.admin;

import be.thomasmore.nudibranch.model.Diet;
import be.thomasmore.nudibranch.model.DietCategory;
import be.thomasmore.nudibranch.repositories.DietCategoryRepository;
import be.thomasmore.nudibranch.repositories.DietRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class DietAdminController {
    private final Logger logger = LoggerFactory.getLogger(DietAdminController.class);

    private final DietRepository dietRepository;
    private final DietCategoryRepository dietCategoryRepository;

    public DietAdminController(DietRepository dietRepository, DietCategoryRepository dietCategoryRepository) {
        this.dietRepository = dietRepository;
        this.dietCategoryRepository = dietCategoryRepository;
    }

    /*-----------------------DIET EDIT PAGE-------------------------------------------------*/
    @ModelAttribute("diet")
    public Diet findDiet(@PathVariable(required = false) Integer dietId) {
        logger.info("find diet : " + dietId);
        if (dietId == null) {
            return new Diet();
        }
        Optional<Diet> optionalDiet = dietRepository.findById(dietId);
        if (optionalDiet.isPresent()) {
            return optionalDiet.get();
        }

        return null;
    }

    //load the diet edit page
    @GetMapping({"/diet-edit/{dietId}"})
    public String editDiet(@PathVariable Integer dietId, Model model) {
        logger.info("get: edit diet with id :{}", dietId);

        return "admin/diet-edit";

    }

    //save edited diet page
    @PostMapping("/diet-edit/{dietId}")
    public String dietEdit(@PathVariable Integer dietId,
                           Model model,
                           @Valid Diet diet,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            logger.info("error editting diet with id :{}", dietId);
            return "admin/diet-edit";
        }
        logger.info("edited diet with id :{}", dietId + "--Diet name = " + diet.getDietCommonName());
        dietRepository.save(diet);
        return "redirect:/diet-details/" + dietId;
    }

    //diet new page
    @GetMapping("/diet-new")
    public String newDiet(Model model) {
        logger.info("get: new diet");

        List<DietCategory> categories = (List<DietCategory>) dietCategoryRepository.findAll();
        model.addAttribute("categories", categories);


        return "admin/diet-new";
    }

    @PostMapping("/diet-new")
    public String dietNew(
            @Valid Diet diet,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("error creating new diet");
            return "admin/diet-new";
        }
        Diet newDiet = dietRepository.save(diet);
        return "redirect:/diet-details/" + newDiet.getDietId();
    }
}
