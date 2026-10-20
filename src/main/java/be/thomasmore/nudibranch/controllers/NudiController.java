package be.thomasmore.nudibranch.controllers;


import be.thomasmore.nudibranch.model.Nudibranch;
import be.thomasmore.nudibranch.repositories.NudiRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class NudiController {
    private final Logger logger = LoggerFactory.getLogger(NudiController.class);
    private static final String NUDIBRANCH_IMG_PATH = "/img/nudis/";
    private final NudiRepository nudiRepository;

    public NudiController(NudiRepository nudiRepository) {
        this.nudiRepository = nudiRepository;
    }


    @GetMapping("/nudi-overview")
    public String nudiOverview(Model model,
                                @RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String commonName,
                                @RequestParam(required = false) String scientificName,
                                @RequestParam(required = false) Boolean venomous) {

        logger.info(String.format("nudi-overview -- keyword=%s, venomous=%s", keyword, venomous));

        // Search venomous + keyword + commonName + scientificName
        List<Nudibranch> nudis = nudiRepository.findByFilters(keyword, commonName,scientificName,venomous);


        // Add image path
        nudis.forEach(nudi -> nudi.setImageUrl(NUDIBRANCH_IMG_PATH + nudi.getImageUrl()));

        // Add attributes for Thymeleaf
        model.addAttribute("nudis", nudis);

        model.addAttribute("keyword", keyword);
        model.addAttribute("commonName", commonName);
        model.addAttribute("scientificName", scientificName);
        model.addAttribute("venomous", venomous);
        model.addAttribute("pageTitle","Nudi Overzicht");

        return "nudi-overview";
    }


    @GetMapping({"/nudi-details/{id}", "/nudi-details"})
    public String details(Model model, @PathVariable(required = false) Integer id) {
        if (id == null) {
            id = 1;
        }

        Optional<Nudibranch> nudiFromDb = nudiRepository.findById(id);
        long count = nudiRepository.count();

        if (nudiFromDb.isPresent()) {
            Nudibranch nudibranch = nudiFromDb.get();
            /*voeg img path toe*/
            nudibranch.setImageUrl(NUDIBRANCH_IMG_PATH + nudibranch.getImageUrl());
            /*  voeg nudi toe aan model*/
            model.addAttribute("nudibranch", nudibranch);

        }

        long prevId = (id > 1) ? id - 1 : count;
        long nextId = (id < count) ? id + 1 : 1;
        model.addAttribute("prevId", prevId);
        model.addAttribute("nextId", nextId);
        model.addAttribute("pageTitle","Nudi Details");

        return "nudi-details";
    }


}
