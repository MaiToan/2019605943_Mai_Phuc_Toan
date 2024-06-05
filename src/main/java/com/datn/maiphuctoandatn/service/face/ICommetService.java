package com.datn.maiphuctoandatn.service.face;

import com.datn.maiphuctoandatn.model.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICommetService {
    public void saveData(Comment new_comment) ;

    public Comment getCommentByProductAndUser(Long product_id, Long user_id);

    public Page<Comment> getCommentByProduct(Long product_id, Integer pageNo, Integer pageSize);

    public List<Comment> GetCommentList(Long product_id);

    public Comment getCommentByID(Long id);

    public List<Comment> getCommentTop();

}
