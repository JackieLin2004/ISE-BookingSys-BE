package com.krowfeather.customer.config;

import com.krowfeather.customer.realm.CustomerRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {
    private final CustomerRealm customerRealm;

    public ShiroConfig(CustomerRealm customerRealm) {
        this.customerRealm = customerRealm;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(3);
        customerRealm.setCredentialsMatcher(credentialsMatcher);
        securityManager.setRealm(customerRealm);
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean
    DefaultShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/api/customers/login", "anon");
        chainDefinition.addPathDefinition("/**", "anon");
        chainDefinition.addPathDefinition("/login", "anon");
        return chainDefinition;
    }
}
