package com.datn.maiphuctoandatn.service.cls;

import com.datn.maiphuctoandatn.model.Author;
import com.datn.maiphuctoandatn.repository.AuthorRepository;
import com.datn.maiphuctoandatn.service.face.IAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService implements IAuthorService {
    @Autowired
    AuthorRepository authorRepository;
    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAllAuthor();
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.getAuthorById(id);
    }

    @Override
    public List<Author> findAuthorBySearch(String search) {
        return authorRepository.getAuthorBySearch(search);
    }
}
