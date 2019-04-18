package net.ion.meteoros21.apiserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter
{
    @Value("${auth-server.client-id}")
    public String CLIENT_ID;
    @Value("${auth-server.secrete}")
    public String SECRETE;
    @Value("${auth-server.resource-id}")
    public String RESOURCE_ID;
    @Value("${auth-server.token-end-point-url}")
    public String TOKEN_END_POINT_URL;
    @Value("${auth-server.jwt-sign-key}")
    String signKey;


    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http.headers().frameOptions().disable();
        http.authorizeRequests()
                .antMatchers("/status").permitAll()
                .antMatchers("/api/**").authenticated()
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception
    {
        resources.resourceId(RESOURCE_ID);
    }

    /**
     * for basic oauth token
    @Bean
    public RemoteTokenServices remoteTokenServices()
    {
        final RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl(TOKEN_END_POINT_URL);
        tokenServices.setClientId(CLIENT_ID);
        tokenServices.setClientSecret(SECRETE);
        return tokenServices;
    }
     */





    @Bean
    public TokenStore tokenStore()
    {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter()
    {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(this.signKey);

        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices()
    {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }
}
