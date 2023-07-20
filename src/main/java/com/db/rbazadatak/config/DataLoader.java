package com.db.rbazadatak.config;

import com.db.rbazadatak.model.ApiUser;
import com.db.rbazadatak.repository.ApiUserRepository;
import com.db.rbazadatak.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// For testing
@Component
public class DataLoader implements ApplicationRunner {

    private final ApiUserRepository apiUserRepository;
    private final PersonRepository personRepository;

    @Autowired
    public DataLoader(ApiUserRepository apiUserRepository, PersonRepository personRepository) {
        this.apiUserRepository = apiUserRepository;
        this.personRepository = personRepository;
    }

    public void run(ApplicationArguments args) {
        apiUserRepository.deleteAll();
        apiUserRepository.save(new ApiUser("apiUser", "password", "USER"));

        personRepository.deleteAll();
    }
}