package be.thomasmore.nudibranch.controllers.admin;

import be.thomasmore.nudibranch.model.Diet;
import be.thomasmore.nudibranch.model.Nudibranch;
import be.thomasmore.nudibranch.repositories.DietRepository;
import be.thomasmore.nudibranch.repositories.NudiRepository;
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
public class NudiAdminController {
    private final Logger logger = LoggerFactory.getLogger(NudiAdminController.class);
    private final NudiRepository nudiRepository;
    private final DietRepository dietRepository;


    public NudiAdminController(NudiRepository nudiRepository, DietRepository dietRepository) {
        this.nudiRepository = nudiRepository;
        this.dietRepository = dietRepository;
    }

    @ModelAttribute("nudibranch")
    public Nudibranch findNudi(@PathVariable(required = false) Integer id) {
        logger.info("findNudibranch -- : " + id);
        if (id == null) {
            return new Nudibranch();
        }
        Optional<Nudibranch> optionalNudibranch = nudiRepository.findById(id);
        if (optionalNudibranch.isPresent()) {
            return optionalNudibranch.get();
        }

        return null;
    }

    //load the nudi edit page
    @GetMapping({"/nudi-edit/{id}"})
    public String editNudi(@PathVariable Integer id, Model model) {
        logger.info("get: edit nudi with id :{}", id);
        //load all nudis for the dropdown
        List<Nudibranch> nudibranches = (List<Nudibranch>) nudiRepository.findAll();

        //voeg lijst van alle dieëten toe zodat dit kan gebruikt w in de dropdown op de edit pagina
        List<Diet> allDiets = (List<Diet>) dietRepository.findAll();

        model.addAttribute("allDiets", allDiets);
        model.addAttribute("nudibranches", nudibranches);

        return "admin/nudi-edit";

    }

    //save nudi edit page
    @PostMapping("/nudi-edit/{id}")
    public String nudiEdit(@PathVariable Integer id,
                           Model model,
                           @Valid Nudibranch nudibranch,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("error editting nudi with id  : " + id);
            return "admin/nudi-edit";
        }

        nudiRepository.save(nudibranch);
        logger.info("nudi edit posted: nudi with id :{}", id );
        return "redirect:/nudi-details/" + id;
    }

  /*  -----------------------------------NEW NUDI--------------------------------*/
    @GetMapping("/nudi-new")
    public String nudiNew (Model model) {
        logger.info("get: new nudi page");
        return "admin/nudi-new";
    }

    @PostMapping("/nudi-new")
    public String nudiNewPost (Model model,
                               @Valid Nudibranch nudibranch,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("error adding new nudi");
            return "admin/nudi-new";
        }
        Nudibranch newNudi = nudiRepository.save(nudibranch);
        logger.info("post : new nudi " +  newNudi.getNudibranchId() +  ": "  + newNudi.getCommonName());
        return "redirect:/nudi-details/" + newNudi.getNudibranchId();
    }

}
