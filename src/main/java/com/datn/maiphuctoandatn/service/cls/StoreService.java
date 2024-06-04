package com.datn.maiphuctoandatn.service.cls;

import com.datn.maiphuctoandatn.model.Store;
import com.datn.maiphuctoandatn.repository.StoreRepository;
import com.datn.maiphuctoandatn.service.face.IStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService implements IStoreService {
    @Autowired
    StoreRepository storeRepository;

    @Override
    public Store findStore() {
        return storeRepository.findAll().get(0);
    }
}
