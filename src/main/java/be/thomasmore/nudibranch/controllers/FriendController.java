package be.thomasmore.nudibranch.controllers;

import be.thomasmore.nudibranch.model.Friend;
import be.thomasmore.nudibranch.repositories.FriendRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class FriendController {
    private final FriendRepository friendRepository;

    public FriendController(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }


    @GetMapping("/friend-overview")
    public String friendOverview(Model model,
                                  @RequestParam(required = false) String friendName,
                                  @RequestParam(required = false) String friendEmail) {
        //filter friends
        List<Friend> friends =  friendRepository.findByFilters(friendName, friendEmail);

        //add attributes to thymeleaf
        model.addAttribute("friends", friends);
        model.addAttribute("pageTitle","Vrienden Overzicht");
        return "friend-overview";
    }

    @GetMapping({"/friend-details", "/friend-details/{friendId}"})
    public String friendDetails(Model model, @PathVariable Integer friendId, Authentication authentication) {
        if (friendId == null) {
            friendId = 1;
        }
        Optional<Friend> friendFromDb = friendRepository.findById(friendId);
        long count = friendRepository.count();

        if (friendFromDb.isPresent()) {
            Friend friend = friendFromDb.get();

            model.addAttribute("friend", friend);

            // Check if logged-in user is the owner of this friend
            boolean canEdit = authentication != null &&
                    authentication.getName().equals(friend.getFriendUsername());
            model.addAttribute("canEdit", canEdit);
        } else {
            model.addAttribute("canEdit", false);
        }

        long prevId = (friendId > 1) ? friendId - 1 : count;
        long nextId = (friendId < count) ? friendId + 1 : 1;
        model.addAttribute("prevId", prevId);
        model.addAttribute("nextId", nextId);
        model.addAttribute("pageTitle","Vrienden Details");


        return "friend-details";
    }
}
