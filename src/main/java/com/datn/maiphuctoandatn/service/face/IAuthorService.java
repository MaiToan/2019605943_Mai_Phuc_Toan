package com.datn.maiphuctoandatn.service.face;

import com.datn.maiphuctoandatn.model.Author;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IAuthorService {

    public List<Author> getAllAuthors();

    Author getAuthorById(Long id);

    public List<Author> findAuthorBySearch(String search);

    public void saveAuthor(Author author);

    public void deleteAuthor(Author author);

    public void updateAuthor(Author author);
}
