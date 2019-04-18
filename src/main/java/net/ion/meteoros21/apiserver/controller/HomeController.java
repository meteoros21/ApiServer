package net.ion.meteoros21.apiserver.controller;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
public class HomeController
{
    @CrossOrigin
    @RequestMapping("/")
    public String root()
    {
        return "Hello World";
    }

    @CrossOrigin
    @RequestMapping("/hello")
    public String hello()
    {
        return "Hello World";
    }

    // 인증 서버의 /oauth/token 의 프록시를 담당하는 메쏘드이다.
    // 필요할 수도 있을 것 같아서..
    @CrossOrigin
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletResponse response,
                        @RequestParam(name = "username") String username,
                        @RequestParam(name = "password") String password,
                        @RequestHeader("Authorization") String test) throws Exception
    {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://127.0.0.1:8081/oauth/token");

        String clientId = null;
        String clientSecrete = null;

        if (test != null && test.length() > 6)
        {
            String a = test.substring(6);
            byte[] c = Base64.decode(a);

            a = new String(c);
            String[] elem = a.split(":");

            clientId = elem[0];
            clientSecrete = elem[1];
        }

        if (clientId != null && clientSecrete != null) {
            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(clientId, clientSecrete);
            httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));

            ArrayList<NameValuePair> postParam = new ArrayList<>();
            postParam.add(new BasicNameValuePair("username", username));
            postParam.add(new BasicNameValuePair("password", password));
            postParam.add(new BasicNameValuePair("scope", "read"));
            postParam.add(new BasicNameValuePair("grant_type", "password"));
            httpPost.setEntity(new UrlEncodedFormEntity(postParam));

            CloseableHttpResponse res = client.execute(httpPost);
            HttpEntity entity = res.getEntity();

            String responseString = EntityUtils.toString(entity);
            client.close();

            return responseString;
        }
        else
        {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            return null;
        }
    }
}
