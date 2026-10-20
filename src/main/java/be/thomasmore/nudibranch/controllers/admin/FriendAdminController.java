package be.thomasmore.nudibranch.controllers.admin;

import be.thomasmore.nudibranch.model.Friend;
import be.thomasmore.nudibranch.repositories.FriendRepository;
import be.thomasmore.nudibranch.services.StorageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@Controller
@RequestMapping("/admin")
public class FriendAdminController {
    private final Logger logger = LoggerFactory.getLogger(FriendAdminController.class);
    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final FriendRepository friendRepository;
    private final StorageService storageService;

    @org.springframework.beans.factory.annotation.Value("${image-bucket-url}")
    private String bucketUrl;

    public FriendAdminController(FriendRepository friendRepository,
                                 JdbcUserDetailsManager jdbcUserDetailsManager,
                                 PasswordEncoder passwordEncoder,
                                 StorageService storageService

    ) {
        this.friendRepository = friendRepository;
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.storageService = storageService;
    }

    @ModelAttribute("friend")
    public Friend findFriend(@PathVariable(required = false) Integer friendId) {
        if (friendId == null) {
            return new Friend();
        }
        Optional<Friend> optionalFriend = friendRepository.findById(friendId);
        if (optionalFriend.isPresent()) {
            return optionalFriend.get();
        }
        return null;
    }

    @GetMapping("/friend-edit/{friendId}")
    public String friendEdit(@PathVariable int friendId, Model model) {

        logger.info("get: edit friend with id :{}", friendId);

        return "admin/friend-edit";
    }

    @PostMapping("/friend-edit/{friendId}")
    public String friendEditPost(@PathVariable int friendId,
                                 @Valid @ModelAttribute("friend") Friend friend,
                                 BindingResult bindingResult,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 Model model) {

        logger.info("post: edit friend with id :{}", friendId);

        if (bindingResult.hasErrors()) {
            logger.info("error editing friend with id :{}", friendId);
            return "admin/friend-edit";
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Upload nieuwe foto en overschrijf de URL in het object
                String fileName = storageService.uploadFile(imageFile);
                friend.setFriendImgUrl(bucketUrl + "/" + fileName);
            } catch (IOException e) {
                logger.error("Upload fout: " + e.getMessage());
            }
        }
        // Als imageFile leeg is, doen we niets.
        // Omdat 'friend' uit de DB kwam via @ModelAttribute,
        // blijft de originele friendImgUrl gewoon in het object staan!

        Friend savedFriend = friendRepository.save(friend);
        return "redirect:/friend-details/" + savedFriend.getFriendId();
    }

    /*-----------------------NEW FRIEND-----------------------*/
    @GetMapping("/friend-new")
    public String friendNew(Model model) {
        logger.info("get: new friend");

        return "admin/friend-new";
    }


    @PostMapping("/friend-new")
    public String friendNewPost(@Valid @ModelAttribute("friend") Friend friend,
                                BindingResult bindingResult,
                                @RequestParam String password) {
        if (bindingResult.hasErrors()) {
            return "admin/friend-new";
        }

        // Save Friend entity
        friendRepository.save(friend);

        // Create Spring Security user
        UserDetails user = org.springframework.security.core.userdetails.User
                .withUsername(friend.getFriendUsername())
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build();
        jdbcUserDetailsManager.createUser(user);

        return "redirect:/friend-details/" + friend.getFriendId();
    }

}
