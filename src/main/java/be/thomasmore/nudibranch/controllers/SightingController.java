package be.thomasmore.nudibranch.controllers;

import be.thomasmore.nudibranch.model.Location;
import be.thomasmore.nudibranch.model.Sighting;
import be.thomasmore.nudibranch.repositories.SightingRepository;
import be.thomasmore.nudibranch.util.DataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SightingController {
    private final Logger logger = LoggerFactory.getLogger(SightingController.class);

    private final SightingRepository sightingRepository;

    public SightingController(SightingRepository sightingRepository) {
        this.sightingRepository = sightingRepository;
    }

    @GetMapping("/sightings")
    public String sightings(Model model) {

        List<Sighting> sightings = (List<Sighting>) sightingRepository.findAll();
//hashmap want voorkomt dupliceren, k(loc) + v(list of S) pairs van locatieA: S1,S2 ,dan locatieB : S5,S6...
        Map<Integer, List<Sighting>> sightingsByLocation = sightings.stream()
                .collect(Collectors.groupingBy(s -> s.getLocation().getLocationId()));


        List<DataPoint> heatmapData = new ArrayList<>();

//voor elke map.entry IN: ...
        for (Map.Entry<Integer, List<Sighting>> entry : sightingsByLocation.entrySet()) {

            Location location = entry.getValue().get(0).getLocation(); // get one Location //getKey = locatieA

            List<Sighting> sightingsAtLocation = entry.getValue(); // S1,S2

            int z = sightingsAtLocation.size();
            logger.info("Aantal sightings op locatie " + location.getLocationDivespot() + " aantal :  " + z);

            String tooltip = sightingsAtLocation.stream()
                    .map(s -> s.getFriend().getFriendName() + " : " + s.getNudibranch().getCommonName())
                    .collect(Collectors.joining(" - ", "Totaal aantal: " + z + " - ", ""));

            heatmapData.add(new DataPoint(location, z, tooltip));
        }

        model.addAttribute("heatmapData", heatmapData);
        model.addAttribute("pageTitle","Waarnemingen Kaart");
        return "sightings";
    }

    @GetMapping("/sightings-overview")
    public String sightingsOverview(Model model,
                                     @RequestParam(required = false) String divespot,
                                     @RequestParam(required = false) String country,
                                     @RequestParam(required = false) String nudibranch) {

        //filters
        logger.info(String.format("sightings filter -- divespot: = %s -- country: %s", divespot, country));
        List<Sighting> filteredSightings = sightingRepository.findByFilter(divespot,country,nudibranch);

        //FILTERED -->group by country
        Map<String, List<Sighting>> sightingsByCountry = filteredSightings.stream()
                .collect(Collectors.groupingBy(sighting -> sighting.getLocation().getLocationCountry()));


        //add attributes for thymeleaf

        model.addAttribute("sightingsByCountry", sightingsByCountry);
        model.addAttribute("divespot", divespot);
        model.addAttribute("country",country);
        model.addAttribute("nudibranch",nudibranch);
        model.addAttribute("pageTitle","Waarnemingen Overzicht");

        return "sightings-overview";
    }
}
