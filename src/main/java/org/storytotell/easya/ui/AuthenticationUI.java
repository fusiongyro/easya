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
package org.storytotell.easya.ui;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashingPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.storytotell.easya.annotations.Logged;
import org.storytotell.easya.annotations.LoggedIn;
import org.storytotell.easya.ejb.AccountManager;
import org.storytotell.easya.entity.User;
import org.storytotell.easya.events.Login;
import org.storytotell.easya.events.Logout;

/**
 * Managed bean to handle login and logout.
 * 
 * @author Daniel Lyons <fusion@storytotell.org>
 */
@RequestScoped
@Logged
@Named("authenticationUI")
public class AuthenticationUI implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private static final Logger log = LoggerFactory.getLogger(AuthenticationUI.class);
  private String username, password;

  @Inject private AccountManager accountManager;
  @Inject private Event<Login>   loginEvent;
  @Inject private Event<Logout>  logoutEvent;
  
  private IniWebEnvironment getWebEnvironment() {
    ServletContext ctx = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
    return (IniWebEnvironment)WebUtils.getRequiredWebEnvironment(ctx);
  }
  
  private PasswordService getPasswordService() {
    return getWebEnvironment().getObject("passwordService", HashingPasswordService.class);
  }
  
  public @Produces @LoggedIn User getLoggedInUser() {
    Subject subject = SecurityUtils.getSubject();
    
    if (!subject.isAuthenticated())
      return null;
    
    return accountManager.findByUsername((String)subject.getPrincipal());
  }
  
  /**
   * Attempt to log in with the currently-defined properties.
   */
  public void authenticate() {
    try {
      SecurityUtils.getSubject().login(getAuthenticationToken());
      log.info("Successful login by {}", SecurityUtils.getSubject().getPrincipal());
      loginEvent.fire(new Login(getLoggedInUser()));
    } catch (Exception e) {
      log.info("Failed login by {}", SecurityUtils.getSubject().getPrincipal());
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Login failed.", "Login failed."));
    }
  }
  
  /**
   * Attempt to log in with these credentials
   * 
   * @param username  the username
   * @param password  the password
   */
  public void login(String username, String password) {
    setUsername(username);
    setPassword(password);
    authenticate();
  }
  
  /**
   * Close the current session for the currently logged-in user.
   */
  public void logout() {
    User u = getLoggedInUser();
    SecurityUtils.getSubject().logout();
    logoutEvent.fire(new Logout(u));
  }
  
  public String getUsername() { return username; }
  public String getPassword() { return password; }

  public void setUsername(String username) { 
    this.username = username; 
  }
  public void setPassword(String password) { 
    this.password = password; 
  }

  /**
   * Produce the authentication token needed by Apache Shiro.
   */
  private AuthenticationToken getAuthenticationToken() {
    return new UsernamePasswordToken(username, password);
  }

  public String encryptPassword(String password) {
    return getPasswordService().encryptPassword(password);
  }
}
