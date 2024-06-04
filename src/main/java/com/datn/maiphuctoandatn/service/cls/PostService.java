package com.datn.maiphuctoandatn.service.cls;

import com.datn.maiphuctoandatn.model.Post;
import com.datn.maiphuctoandatn.repository.PostRepository;
import com.datn.maiphuctoandatn.service.face.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class PostService implements IPostService {

    @Autowired
    PostRepository postRepository;
    @Override
    public Post getPost() {
        Random random = new Random();
        List<Post> posts = postRepository.getAll();
        return posts.get(random.nextInt(posts.size()));
    }
}
