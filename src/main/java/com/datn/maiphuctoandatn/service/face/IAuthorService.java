package com.datn.maiphuctoandatn.service.face;

import com.datn.maiphuctoandatn.model.Author;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IAuthorService {

    public List<Author> getAllAuthors();

    Author getAuthorById(Long id);

    public List<Author> findAuthorBySearch(String search);
}
