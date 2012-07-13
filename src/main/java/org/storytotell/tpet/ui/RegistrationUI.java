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
package org.storytotell.tpet.ui;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.authc.credential.PasswordService;
import org.storytotell.tpet.ejb.AccountManager;
import org.storytotell.tpet.entity.User;

/**
 * @author Daniel Lyons <fusion@storytotell.org>
 */
@RequestScoped
@Named("registrationUI")
public class RegistrationUI implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String username, email, password, passwordConfirmation, accountType;

  public String getUsername()             { return username; }
  public String getEmail()                { return email; }
  public String getPassword()             { return password; }
  public String getPasswordConfirmation() { return passwordConfirmation; }
  public String getAccountType()          { return accountType; }

  public void setUsername(String username)                 { this.username = username; }
  public void setEmail(String email)                       { this.email = email; }
  public void setPassword(String password)                 { this.password = password; }
  public void setPasswordConfirmation(String confirmation) { this.passwordConfirmation = confirmation; }
  public void setAccountType(String accountType)           { this.accountType = accountType; }
  
  private @Inject AuthenticationUI authenticationUI;
  private @Inject PasswordService  passwordService;
  private @Inject AccountManager accountManager;
  
  public String register() {
    User user = new User();

    String encryptedPassword = passwordService.encryptPassword(password);

    user.setUsername(username);
    user.setPassword(encryptedPassword);
    user.setEmailAddress(email);
    
    accountManager.register(user);
    
    login();
    return ""; // return "pretty:user"
  }

  private void login() {
    authenticationUI.login(username, password);
  }
}
