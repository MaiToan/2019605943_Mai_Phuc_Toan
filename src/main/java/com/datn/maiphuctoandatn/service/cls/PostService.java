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

    @Override
    public List<Post> getAll() {
        return postRepository.getAll();
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public void delete(Long id) {
        Post post = postRepository.findById(id).get();
        post.setLOEVM("1");
        postRepository.save(post);
    }
}
