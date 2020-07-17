package com.github.camelya58.auth_with_google.repository;

import com.github.camelya58.auth_with_google.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface NoteRepository is responsible for connecting to PostgreSQL.
 *
 * @author Kamila Meshcheryakova
 * created 17.07.2020
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    /**
     * This method is used to find list of notes by user id.
     *
     * @param userId th user identifier
     * @return list of notes
     */
    List<Note> findByUserId(Long userId);
}
