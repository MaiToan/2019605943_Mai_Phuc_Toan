package com.datn.maiphuctoandatn.service.face;

import com.datn.maiphuctoandatn.model.Favorite;

import java.util.List;

public interface IFavoriteService {

    public Favorite getFavoriteByUserProduct(Long id_product, Long id_user);

    public void remove(Favorite favorite);

    public void save(Favorite favorite);

    public List<Favorite> getAllFavorites();

    public List<Favorite> getAllFavoritesByUser(Long user_id);
}
