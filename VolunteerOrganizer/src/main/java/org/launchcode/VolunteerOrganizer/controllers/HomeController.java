package org.launchcode.VolunteerOrganizer.controllers;

import org.launchcode.VolunteerOrganizer.models.Opportunity;
import org.launchcode.VolunteerOrganizer.models.OpportunityData;
import org.launchcode.VolunteerOrganizer.models.User;
import org.launchcode.VolunteerOrganizer.models.data.OpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;

@RequestMapping("home")
@Controller
public class HomeController {

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    AuthenticationController authenticationController;

    @GetMapping("")
    public String displayHome(Model model) {
        model.addAttribute("title", "Home");
        return "home";
    }

    @PostMapping("/results")
    public String displaySearchResults(Model model, @RequestParam String searchTerm, @RequestParam String category, @RequestParam String start, @RequestParam String end) throws ParseException {
        Iterable<Opportunity> opportunities;

        opportunities = OpportunityData.findBySearchTerm(searchTerm, opportunityRepository.findAll());
        opportunities = OpportunityData.findByCategory(category, opportunities);
        opportunities = OpportunityData.findByDate(start, end, opportunities);

        model.addAttribute("title", "Home");
        model.addAttribute("resultsTitle", "Search results:");
        model.addAttribute("opportunities", opportunities);

        return "home";
    }

    @GetMapping("/redirect")
    public String displayHomeRedirect(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);

        model.addAttribute("title", "Home");
        model.addAttribute("redirectMessage", "Access Denied as " + user.getAccountType().substring(0, 1).toUpperCase() + user.getAccountType().substring(1) + ": Redirected to Home");
        return "home";
    }

}