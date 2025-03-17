package com.sokratis.ExpenseTracker.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.sokratis.ExpenseTracker.Repository.CategoryRepository;
import com.sokratis.ExpenseTracker.Repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public DataLoader(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = null;
    }

    @Override
    public void run(String... args) throws Exception {
        // Load initial data into the database
        // userRepository.save(new User("John Doe", "john@example.com", "1234"));
        // userRepository.save(new User("Jane Smith", "jane@example.com", "1234"));

        // categoryRepository.save(new Category("FOOD"));
    }
}
