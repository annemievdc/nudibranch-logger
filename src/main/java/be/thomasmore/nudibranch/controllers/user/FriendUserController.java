package be.thomasmore.nudibranch.controllers.user;

import be.thomasmore.nudibranch.model.Friend;
import be.thomasmore.nudibranch.repositories.FriendRepository;
import be.thomasmore.nudibranch.services.StorageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class FriendUserController {

    private final Logger logger = LoggerFactory.getLogger(FriendUserController.class);
    private final FriendRepository friendRepository;
    private final StorageService storageService;

    @org.springframework.beans.factory.annotation.Value("${image-bucket-url}")
    private String bucketUrl;

    public FriendUserController(FriendRepository friendRepository, StorageService storageService) {
        this.friendRepository = friendRepository;
        this.storageService = storageService;
    }


    @ModelAttribute("friend")
    public Friend loadFriend(Authentication authentication) {
        String username = authentication.getName();
        Optional<Friend> optionalFriend = friendRepository.findByFriendUsername(username)
                .stream().findFirst();

        if (optionalFriend.isPresent()) {
            return optionalFriend.get();
        }
        // Should never happen if user is logged in properly
        logger.warn("Friend not found for username: {}", username);

        return null;
    }

    @GetMapping("/friend-edit")
    public String friendEdit() {
        // The friend is already loaded in the model via @ModelAttribute
        logger.info("User editing their own details");
        return "user/friend-edit";
    }

    @PostMapping("/friend-edit")
    public String friendEditPost(@Valid @ModelAttribute("friend") Friend friend,
                                 BindingResult bindingResult,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 Authentication authentication,
                                 Model model) {
        String username = authentication.getName();

        if (bindingResult.hasErrors()) {
            logger.info("Validation errors while editing friend with username {}", username);
            return "user/friend-edit";
        }

        // Verify that the logged-in user matches the friend
        if (!username.equals(friend.getFriendUsername())) {
            throw new SecurityException("Cannot edit another user's details!");
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = storageService.uploadFile(imageFile);
                friend.setFriendImgUrl(bucketUrl + "/" + fileName);
                logger.info("Nieuwe foto geüpload voor user {}: {}", username, fileName);
            } catch (java.io.IOException e) {
                logger.error("Upload fout voor user {}: {}", username, e.getMessage());
                model.addAttribute("errorMessage", "Fout bij uploaden van foto.");
                return "user/friend-edit";
            }
        }
        // Als imageFile leeg is, doen we niets met friendImgUrl.
        // Omdat 'friend' via @ModelAttribute uit de DB kwam, blijft de oude waarde behouden!

        // Save updated friend
        Friend savedFriend = friendRepository.save(friend);
        logger.info("Saved friend details for username {}", username);

        return "redirect:/friend-details/" + savedFriend.getFriendId();
    }
}
