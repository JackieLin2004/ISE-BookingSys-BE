package com.krowfeather.customer.realm;

import com.krowfeather.customer.entity.Customer;
import com.krowfeather.customer.service.CustomerService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

@Component
public class CustomerRealm extends AuthorizingRealm {
    private final CustomerService customerService;
    public CustomerRealm(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username  = authenticationToken.getPrincipal().toString();
        Customer customer =  this.customerService.getByUsername(username);
        if(customer != null){
            AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    authenticationToken.getPrincipal(),
                    customer.getPassword(),
                    ByteSource.Util.bytes("salt"),
                    authenticationToken.getPrincipal().toString()
            );
            return authenticationInfo;
        }
        return null;
    }
}
