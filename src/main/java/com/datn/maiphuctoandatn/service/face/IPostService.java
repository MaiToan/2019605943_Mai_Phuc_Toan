package com.datn.maiphuctoandatn.service.face;

import com.datn.maiphuctoandatn.model.Post;

import java.util.List;

public interface IPostService {
    public Post getPost();
    public List<Post> getAll();
    public  void save(Post post);
    public  void delete(Long id);
}
