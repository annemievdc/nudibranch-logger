package be.thomasmore.nudibranch.controllers.admin;

import be.thomasmore.nudibranch.model.Friend;
import be.thomasmore.nudibranch.model.Location;
import be.thomasmore.nudibranch.model.Nudibranch;
import be.thomasmore.nudibranch.model.Sighting;
import be.thomasmore.nudibranch.repositories.FriendRepository;
import be.thomasmore.nudibranch.repositories.LocationRepository;
import be.thomasmore.nudibranch.repositories.NudiRepository;
import be.thomasmore.nudibranch.repositories.SightingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class SightingAdminController {

    private final Logger logger = LoggerFactory.getLogger(SightingAdminController.class);

    private final SightingRepository sightingRepository;
    private final LocationRepository locationRepository;
    private final FriendRepository friendRepository;
    private final NudiRepository nudiRepository;

    public SightingAdminController(
            SightingRepository sightingRepository,
            LocationRepository locationRepository,
            FriendRepository friendRepository,
            NudiRepository nudiRepository) {

        this.sightingRepository = sightingRepository;
        this.locationRepository = locationRepository;
        this.friendRepository = friendRepository;
        this.nudiRepository = nudiRepository;
    }



    /*-------------------------------NEW PAGE----------------------------------------------------*/
    @GetMapping("/sighting-new")
    public String sightingNew(Model model) {
        logger.info("new sighting form opened");

        model.addAttribute("allFriends", friendRepository.findAll());
        model.addAttribute("allLocations", locationRepository.findAll());
        model.addAttribute("allNudibranches", nudiRepository.findAll());
        model.addAttribute("sighting", new Sighting());
        model.addAttribute("pageTitle","Nieuwe Waarneming");

        return "admin/sighting-new";
    }

    @PostMapping("/sighting-new")
    public String sightingNewSave(@ModelAttribute Sighting sighting) {

        Location location = locationRepository.findById(
                        sighting.getLocation().getLocationId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid location ID"));

        Friend friend = friendRepository.findById(
                        sighting.getFriend().getFriendId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid friend ID"));

        Nudibranch nudibranch = nudiRepository.findById(
                        sighting.getNudibranch().getNudibranchId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid nudibranch ID"));

        sighting.setLocation(location);
        sighting.setFriend(friend);
        sighting.setNudibranch(nudibranch);

        sightingRepository.save(sighting);

        return "redirect:/sightings-overview";
    }
}
