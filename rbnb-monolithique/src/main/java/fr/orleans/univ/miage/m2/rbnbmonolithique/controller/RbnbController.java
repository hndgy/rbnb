package fr.orleans.univ.miage.m2.rbnbmonolithique.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class RbnbController {

    @RolesAllowed("user")
    @GetMapping("hello")
    public String hello(Principal principal){
        return principal.toString();
    }


    @GetMapping("norole")
    public String noauth(Principal principal){
        return principal.toString();
    }

    @RolesAllowed("ADMIN")
    @GetMapping("admin")
    public String admin(Principal principal){
        return principal.getName();
    }
}
