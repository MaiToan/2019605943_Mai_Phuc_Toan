package com.datn.maiphuctoandatn.service.cls;

import com.datn.maiphuctoandatn.model.Favorite;
import com.datn.maiphuctoandatn.repository.FavoriteRepository;
import com.datn.maiphuctoandatn.service.face.IFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService implements IFavoriteService {

    @Autowired
    FavoriteRepository favoriteRepository;

    @Override
    public Favorite getFavoriteByUserProduct(Long id_product, Long id_user) {
        return favoriteRepository.getFavoriteByUserProduct(id_user, id_product);
    }

    @Override
    public void remove(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }

    @Override
    public void save(Favorite favorite) {
        favoriteRepository.save(favorite);
    }

    @Override
    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    @Override
    public List<Favorite> getAllFavoritesByUser(Long user_id) {
        return favoriteRepository.getFavoriteByUser(user_id);
    }
}
