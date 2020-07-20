package com.github.camelya58.auth_with_google.model;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Class Note is an entity of database "googledb", has id, title, content as note and id of the user who owns it.
 *
 * @author Kamila Meshcheryakova
 * created 17.07.2020
 */
@Data
@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String note;

    @NotNull
    private Long userId;
}
