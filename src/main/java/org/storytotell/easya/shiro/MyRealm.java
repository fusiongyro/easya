/*
 * Copyright (c) 2012, Daniel Lyons
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package org.storytotell.easya.shiro;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.storytotell.easya.ejb.AccountManager;
import org.storytotell.easya.entity.User;

/**
 *
 * @author Daniel Lyons <fusion@storytotell.org>
 */
public class MyRealm extends AuthorizingRealm {
  private PasswordService passwordService = null;

  public PasswordService getPasswordService() { return this.passwordService; }
  public void setPasswordService(PasswordService service) { this.passwordService = service; }

  private AccountManager getAccountManager() {
    try {
      InitialContext ic = new InitialContext();
      return (AccountManager) ic.lookup("java:module/AccountManager");
    } catch (NamingException ex) {
      Logger.getLogger(MyRealm.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
  
  @Override 
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) 
          throws AuthenticationException {
    // find this user
    User u = getAccountManager().findByUsername((String)token.getPrincipal());
    
    if (u == null)
      throw new UnknownAccountException();
    
    return new SimpleAuthenticationInfo(token.getPrincipal(), u.getPassword(), getName());
  }

  @Override 
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    // find this user
    User u = getAccountManager().findByUsername((String)principals.getPrimaryPrincipal());
    
    // generate the result
    SimpleAuthorizationInfo authz = new SimpleAuthorizationInfo();

    // copy the user's specific permissions into the auth info as well
    u.copyPermissionsInto(authz);
    
    // put the standard permissions on this auth info
    copyStandardAuthenticatedUserPermissionsInto(authz);
    
    clearCachedAuthorizationInfo(principals);
    
    return authz;
  }

  private void copyStandardAuthenticatedUserPermissionsInto(SimpleAuthorizationInfo authz) {
    // anybody can create a course
    authz.addStringPermission("course:create");
  }
}
