package be.thomasmore.nudibranch.controllers.user;

import be.thomasmore.nudibranch.model.Friend;
import be.thomasmore.nudibranch.model.Sighting;
import be.thomasmore.nudibranch.repositories.FriendRepository;
import be.thomasmore.nudibranch.repositories.LocationRepository;
import be.thomasmore.nudibranch.repositories.NudiRepository;
import be.thomasmore.nudibranch.repositories.SightingRepository;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
@RequestMapping("/user")
public class SightingUserController {

    private final FriendRepository friendRepository;
    private final LocationRepository locationRepository;
    private final NudiRepository nudiRepository;
    private final SightingRepository sightingRepository;

    public SightingUserController(FriendRepository friendRepository,
                                  LocationRepository locationRepository,
                                  NudiRepository nudiRepository,
                                  SightingRepository sightingRepository) {
        this.friendRepository = friendRepository;
        this.locationRepository = locationRepository;
        this.nudiRepository = nudiRepository;
        this.sightingRepository = sightingRepository;
    }

    // -----------------------------
    // MODEL ATTRIBUTE FOR CURRENT FRIEND
    // -----------------------------
    @ModelAttribute("currentFriend")
    public Friend getCurrentFriend(Principal principal) {
        if (principal == null) return null; // no user logged in
        return friendRepository.findByFriendUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Friend not found"));
    }

    // -----------------------------
    // NEW SIGHTING FORM
    // -----------------------------
    @GetMapping("/sighting-new")
    public String sightingNewForm(Model model) {
        model.addAttribute("sighting", new Sighting());
        model.addAttribute("allLocations", locationRepository.findAll());
        model.addAttribute("allNudibranches", nudiRepository.findAll());
        return "user/sighting-new";
    }

    // -----------------------------
    // SAVE SIGHTING
    // -----------------------------
    @PostMapping("/sighting-new")
    public String sightingNewSubmit(@Valid @ModelAttribute("sighting") Sighting sighting,
                                    BindingResult bindingResult,
                                    @ModelAttribute("currentFriend") Friend currentFriend,
                                    Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allLocations", locationRepository.findAll());
            model.addAttribute("allNudibranches", nudiRepository.findAll());
            return "user/sighting-new";
        }

        // Associate the sighting with the current friend
        sighting.setFriend(currentFriend);

        // Save to the database
        sightingRepository.save(sighting);

        return "redirect:/sightings-overview";
    }
}
