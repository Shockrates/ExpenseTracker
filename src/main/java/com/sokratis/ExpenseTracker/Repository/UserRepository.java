package com.sokratis.ExpenseTracker.Repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.sokratis.ExpenseTracker.Model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
