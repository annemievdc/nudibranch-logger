package be.thomasmore.nudibranch.controllers;

import be.thomasmore.nudibranch.model.Friend;
import be.thomasmore.nudibranch.repositories.FriendRepository;
import be.thomasmore.nudibranch.services.StorageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
public class UserController {
    private final FriendRepository friendRepository;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    @Value("${image-bucket-url}") // De basis-URL uit je properties
    private String bucketUrl;

    public UserController(FriendRepository friendRepository,
                          JdbcUserDetailsManager jdbcUserDetailsManager,
                          PasswordEncoder passwordEncoder,
                          StorageService storageService) {
        this.friendRepository = friendRepository;
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.storageService = storageService;
    }

    /* Principal represents the currently authenticated user.*/
    @GetMapping("/login")
    public String login(
            Model model,
            Principal principal,
            @RequestParam(required = false) String error
    ) {
        // If already logged in, redirect to /home
        if (principal != null) return "redirect:/home";

        // Show error message if login failed
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password!");
        }

        model.addAttribute("pageTitle", "Login");

        return "user/login";
    }


    @GetMapping("/logout")
    public String logoutPage(Principal principal,Model model) {
        if (principal == null) {
            return "redirect:/login"; // redirect if not logged in
        }
        model.addAttribute("pageTitle", "Logout");
        return "user/logout";
    }

    @GetMapping("/register")
    public String registerGet(Principal principal, Model model) {
        if (principal != null) return "redirect:/home";

        model.addAttribute("friend", new Friend());
        model.addAttribute("pageTitle", "Register");

        return "user/register";
    }


    @PostMapping("/register")
    public String registerPost(Principal principal,
                               HttpServletRequest request,
                               @Valid @ModelAttribute("friend") Friend friend,
                               BindingResult bindingResult,
                               @RequestParam String password,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               Model model) throws ServletException {
        //als er al een user ingelogd is, keer terug naar home
        if (principal != null) return "redirect:/home";
        //binding errors ? ga terug naar register
        if (bindingResult.hasErrors()) {
            return "user/register";
        }

        //bestaat de username al ? ga terug naar register
        if (jdbcUserDetailsManager.userExists(friend.getFriendUsername())) {
            bindingResult.rejectValue("friendUsername",
                    "error.friendUsername",
                    "Deze username bestaat al");
            model.addAttribute("errorMessage", "Username exists!");
            model.addAttribute("pageTitle", "Register");
            return "user/register";
        }
        // --- START UPLOAD LOGICA ---
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // 1. Upload naar Supabase
                String fileName = storageService.uploadFile(imageFile);
                // 2. Sla de volledige Cloud URL op in het Friend object
                friend.setFriendImgUrl(bucketUrl + "/" + fileName);
            } catch (IOException e) {
                model.addAttribute("errorMessage", "Fout bij uploaden afbeelding.");
                return "user/register";
            }
        } else {
            // Optioneel: Standaard afbeelding als ze niets uploaden
            friend.setFriendImgUrl("default-user.jpg");
        }
        // --- EINDE UPLOAD LOGICA ---


        //maak user aan
        UserDetails user = org.springframework.security.core.userdetails.User
                .withUsername(friend.getFriendUsername())
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build();
        jdbcUserDetailsManager.createUser(user);
        //save friend entity
        friendRepository.save(friend);

        //autologin
        request.login(friend.getFriendUsername(), password);
        return "redirect:/home";

    }
}

