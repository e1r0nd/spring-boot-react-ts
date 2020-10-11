package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequestMapping("api/v1")
@RestController
public class UserNameController {
    @Autowired
    private UserNameRepository userRepository;

    @GetMapping("/get")
    public List<String> get() {
        Iterable<UserName> userNames = userRepository.findAll();
        Iterator<UserName> iterator = userNames.iterator();
        List<String> output = new ArrayList<>();

        while (iterator.hasNext()) {
            UserName userName = iterator.next();
            output.add(userName.getName());
        }
        return output;
    }

    @PostMapping("/create")
    public String create(@RequestParam(value = "name", defaultValue = "userName") String name) {
        UserName userName = new UserName(name);
        userRepository.save(userName);

        Iterable<UserName> userNames = userRepository.findAll();
        return userNames.toString();
    }
}
