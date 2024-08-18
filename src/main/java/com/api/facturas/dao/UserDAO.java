package com.api.facturas.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;

import com.api.facturas.wrapper.UserWrapper;
import com.api.facturas.pojo.User;
import java.util.List;

@Repository
public interface UserDAO extends JpaRepository<User, Integer>{
	
	 User findByEmail(@Param(("email")) String email);
/*
	   List<UserWrapper> getAllUsers();

    List<String> getAllAdmins();

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status,@Param("id") Integer id);
    */
}
