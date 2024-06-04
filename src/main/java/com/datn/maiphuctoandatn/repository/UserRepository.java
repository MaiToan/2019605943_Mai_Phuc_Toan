package com.datn.maiphuctoandatn.repository;

import com.datn.maiphuctoandatn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users  WHERE email = :email and password =:password", nativeQuery = true)
    User getUserByEmailAndPassword(String email, String password);

    @Query(value = "SELECT * FROM users   WHERE email = :email", nativeQuery = true)
    User checkEmail(String email);

    @Query(value = "SELECT * FROM users WHERE id = :id", nativeQuery = true)
    User FindUser(Long id);

    @Query(value = "SELECT COUNT(*) FROM users WHERE MONTH(created_at) = MONTH(CURRENT_TIMESTAMP) AND YEAR(created_at) = YEAR(CURRENT_TIMESTAMP)", nativeQuery = true)
    Integer getUserMonthCurrent();

    @Query(value = "SELECT COUNT(*) FROM users WHERE MONTH(created_at) = MONTH(CURRENT_TIMESTAMP)-1 AND YEAR(created_at) = YEAR(CURRENT_TIMESTAMP)", nativeQuery = true)
    Integer getUserLastMonth();

    @Query(value = "SELECT * FROM users WHERE name like %:search% OR email like %:search% OR phone like %:search% OR address like %:search%", nativeQuery = true)
    List<User> GetUserBySearch(String search);

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    User getUserByEmail(String email);

    @Query(value = "SELECT * FROM users order by active desc ", nativeQuery = true)
    List<User> GetAllUser();
}