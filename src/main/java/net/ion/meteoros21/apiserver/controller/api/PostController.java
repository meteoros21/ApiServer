package net.ion.meteoros21.apiserver.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ion.meteoros21.apiserver.entity.Post;
import net.ion.meteoros21.apiserver.entity.User;
import net.ion.meteoros21.apiserver.repository.PostRepository;
import net.ion.meteoros21.apiserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
public class PostController
{
    public static int ROWS_PER_PAGE = 10;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceServerTokenServices tokenServices;

    //@CrossOrigin
    @RequestMapping("/")
    public String postRoot()
    {
        return "redirect:/api/post/list";
    }

    //@CrossOrigin
    @GetMapping(value = "/list")
    public String list(@RequestParam(name="page", defaultValue = "1") int page,
                       @RequestParam(name="rpp", defaultValue = "0") int rowsPerPage) throws Exception
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("------------------------------------");
        System.out.println(authentication.getName());

        String tokenValue = ((OAuth2AuthenticationDetails)authentication.getDetails()).getTokenValue();
        Map<String, Object> additionalUserInfo = tokenServices.readAccessToken(tokenValue).getAdditionalInformation();
        System.out.println(additionalUserInfo.get("realName"));

        List<GrantedAuthority> authorityList = new ArrayList<>(authentication.getAuthorities());
        for (GrantedAuthority authority : authorityList)
        {
            if (authority.getAuthority().equals("ROLE_TRUSTED_CLIENT"))
            {
                System.out.println("has ROLE_TRUSTED_CLIENT");
            }
        }
        System.out.println("------------------------------------");

        rowsPerPage = (rowsPerPage == 0) ? ROWS_PER_PAGE : rowsPerPage;
        PageRequest p = PageRequest.of(page-1, rowsPerPage, Sort.Direction.DESC, "postId");
        Page<Post> pageInfo = postRepository.findAllJoinFetch(p);

        HashMap<String, Object> result = new HashMap<>();
        result.put("rowCount", pageInfo.getTotalElements());
        result.put("postList", pageInfo.getContent());

        ObjectMapper mapper = new ObjectMapper();
        //return mapper.writeValueAsString(pageInfo.getContent());
        return mapper.writeValueAsString(result);
    }

    @GetMapping(value = "/post")
    public String post(@RequestParam(name="postId") int postId) throws Exception
    {
        Post post = postRepository.findOneByPostId(postId);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(post);
    }

    @CrossOrigin
    @PostMapping(value = "update")
    public String update(@RequestParam(name="postId", defaultValue = "0") int postId,
                         @RequestParam(name="title") String title,
                         @RequestParam(name="postBody") String postBody,
                         @RequestParam(name="userId") String userId) throws Exception
    {
        HashMap<String, Object> result = new HashMap<>();

        if (postId != 0)
        {
            Post post = postRepository.findOneByPostId(postId);
            if (post.getUserId().equals(userId)) {
                post.setTitle(title);
                post.setBody(postBody);

                Post newPost = postRepository.save(post);

                result.put("result", true);
                result.put("post", newPost);
            }
            else
            {
                result.put("result", false);
                result.put("msg", "invalid user");
            }
        }
        else
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            User user = userRepository.findOneByUserId(authentication.getName());
            Post post = new Post();
            post.setUser(user);
            post.setTitle(title);
            post.setBody(postBody);

            Post newPost = postRepository.save(post);
            newPost.setUser(user);

            result.put("result", true);
            result.put("post", newPost);
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @CrossOrigin
    @RequestMapping("/hello")
    public String hello()
    {
        return "Hello World2222 /api/post/hello";
    }
}
