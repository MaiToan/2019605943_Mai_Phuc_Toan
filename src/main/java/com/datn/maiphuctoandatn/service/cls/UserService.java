package com.datn.maiphuctoandatn.service.cls;

import com.datn.maiphuctoandatn.model.User;
import com.datn.maiphuctoandatn.repository.UserRepository;
import com.datn.maiphuctoandatn.service.face.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements IUserService, UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User Authentication(String email, String password) {
        return userRepository.getUserByEmailAndPassword(email, password);
    }

    @Override
    public User CheckEmail(String email) {
        return userRepository.checkEmail(email);
    }

    @Override
    public User SaveData(User user) {
        return userRepository.save(user);
    }

    @Override
    public String getMD5(String password) {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            // of an input digest() return array of byte
            byte[] messageDigest = md.digest(password.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.GetAllUser();
    }

    @Override
    public User updateUser(User user) {
        User user_new = userRepository.FindUser(user.getId());
        user_new.setName(user.getName());
        user_new.setAddress(user.getAddress());
        user_new.setAge(user.getAge());
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            user_new.setAvatar(user.getAvatar());
        }
        user_new.setGender(user.getGender());
        return userRepository.save(user_new);
    }

    @Override
    public User changePass(User user, String pass) {
        User userDB = userRepository.FindUser(user.getId());
        userDB.setPassword(pass);
        return userRepository.save(userDB);
    }

    @Override
    public Integer getPercentUser() {
        Integer userMonthCurrent = userRepository.getUserMonthCurrent();
        Integer userlastMonth = userRepository.getUserLastMonth();
        return (userMonthCurrent - userlastMonth) * 100 / userlastMonth;
    }

    @Override
    public List<User> findUserBySearch(String search) {
        return userRepository.GetUserBySearch(search);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(user.getPassword())
                .authorities(user.getRole()).build();
    }
    @Override
    public Boolean checkPassword(String passOld, String passConfirm){
        return passwordEncoder.matches(passOld, passConfirm);
    }

    @Override
    public User findUserByID(Long id) {
        return userRepository.findById(id).get();
    }
}


