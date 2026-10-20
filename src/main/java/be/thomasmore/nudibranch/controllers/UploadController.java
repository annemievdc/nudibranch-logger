package be.thomasmore.nudibranch.controllers;

import be.thomasmore.nudibranch.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class UploadController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/upload")
    public String upload(Model model) {
        model.addAttribute("pageTitle", "Upload Test");
        return "upload";
    }

    @PostMapping("/upload")
    public String handleUpload(@RequestParam("imageInput") MultipartFile file, Model model) {
        try {
            if (!file.isEmpty()) {
                // De service uploadt de file en geeft de naam terug
                String fileName = storageService.uploadFile(file);
                model.addAttribute("imageUrl", fileName);
            }
        } catch (IOException e) {
            model.addAttribute("error", "Fout bij uploaden: " + e.getMessage());
        }
        model.addAttribute("pageTitle", "Upload Nudi");
        return "upload";
    }
}
