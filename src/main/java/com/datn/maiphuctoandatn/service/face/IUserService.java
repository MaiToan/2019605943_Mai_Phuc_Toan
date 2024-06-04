package com.datn.maiphuctoandatn.service.face;

import com.datn.maiphuctoandatn.model.User;

import java.util.List;

public interface IUserService {

    public User Authentication(String email, String password) ;

    public User CheckEmail(String email) ;

    public User SaveData(User user) ;

    public String getMD5(String password) ;

    public List<User> getAllUsers() ;

    public User updateUser(User user);

    public User changePass(User user, String pass);

    public Integer getPercentUser();

    public List<User> findUserBySearch(String search) ;

    public Boolean checkPassword(String passOld, String passConfirm);

    public User findUserByID(Long id) ;
}
