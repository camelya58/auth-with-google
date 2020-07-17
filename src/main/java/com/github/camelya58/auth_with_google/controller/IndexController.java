package com.github.camelya58.auth_with_google.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * Class IndexController is a enter to main page with registration or identification by login,
 * for already registered redirects to the page with list of notes.
 *
 * @author Kamila Meshcheryakova
 * created 17.07.2020
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Principal principal) {
        if (principal != null) {
            return "redirect:/notes";
        }
        return "index";
    }
}
