package net.ion.meteoros21.apiserver.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ion.meteoros21.apiserver.entity.User;
import net.ion.meteoros21.apiserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController
{
    @Autowired
    UserRepository userRepository;

    @GetMapping("/get")
    public String getUser(@RequestParam(name="userId") String userId) throws Exception
    {
        User user = userRepository.findOneByUserId(userId);
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("result", true);

        return mapper.writeValueAsString(result);
    }
}
