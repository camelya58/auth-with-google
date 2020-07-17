package com.github.camelya58.auth_with_google.controller;

import com.github.camelya58.auth_with_google.model.Note;
import com.github.camelya58.auth_with_google.model.User;
import com.github.camelya58.auth_with_google.repository.NoteRepository;
import com.github.camelya58.auth_with_google.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

/**
 * Class NoteController allows already registered user to get list of his notes or to add new note.
 *
 * @author Kamila Meshcheryakova
 * created 17.07.2020
 */
@Controller
public class NoteController {

    @Autowired
    private UserService userService;

    @Autowired
    private NoteRepository noteRepository;

    @GetMapping("/notes")
    public String notes(Principal principal, Model model) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        List<Note> notes = noteRepository.findByUserId(user.getId());
        model.addAttribute("notes", notes);
        model.addAttribute("user", user);
        return "notes";
    }

    @PostMapping("/addnote")
    public String addNote(Principal principal, String title, String note) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        Note newNote = new Note();
        newNote.setTitle(title);
        newNote.setNote(note);
        newNote.setUserId(user.getId());

        noteRepository.save(newNote);

        return "redirect:/notes";
    }
}
