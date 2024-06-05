package com.datn.maiphuctoandatn.service.cls;

import com.datn.maiphuctoandatn.model.Comment;
import com.datn.maiphuctoandatn.model.Product;
import com.datn.maiphuctoandatn.repository.CommentRepository;
import com.datn.maiphuctoandatn.service.face.ICommetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService implements ICommetService {

    @Autowired
    CommentRepository commentRepository ;

    @Override
    public void saveData(Comment new_comment) {
        commentRepository.save(new_comment);
    }

    @Override
    public Comment getCommentByProductAndUser(Long product_id, Long user_id) {
        return commentRepository.ckeComment(product_id, user_id);
    }

    @Override
    public Page<Comment> getCommentByProduct(Long product_id, Integer pageNo, Integer pageSize) {
        List<Comment> list =  commentRepository.GetCommentByProduct(product_id);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());
        List<Comment> subList = list.subList(start, end);
        return new PageImpl<Comment>(subList, pageable, list.size());
    }

    @Override
    public List<Comment> GetCommentList(Long product_id) {
        return  commentRepository.GetCommentByProduct(product_id);
    }

    @Override
    public Comment getCommentByID(Long id) {
        return commentRepository.findById(id).get();
    }

    @Override
    public List<Comment> getCommentTop() {
        return commentRepository.getTopComment();
    }
}
