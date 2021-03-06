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
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import org.storytotell.easya.annotations.Logged;
import org.storytotell.easya.ejb.AccountManager;
import org.storytotell.easya.entity.User;

/**
 * Handle the form and process of registering a new account.
 * 
 * @author Daniel Lyons <fusion@storytotell.org>
 */
@Named("registrationUI")
@Logged
@RequestScoped
public class RegistrationUI implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private @NotNull String username;
  private @NotNull String email;
  private @NotNull String password;
  private String accountType;

  public String getUsername()             { return username; }
  public String getEmail()                { return email; }
  public String getPassword()             { return password; }
  public String getAccountType()          { return accountType; }

  public void setUsername(String username)                 { this.username = username; }
  public void setEmail(String email)                       { this.email = email; }
  public void setPassword(String password)                 { this.password = password; }
  public void setAccountType(String accountType)           { this.accountType = accountType; }
  
  private @Inject AuthenticationUI authenticationUI;
  private @Inject AccountManager accountManager;
  
  public String register() {
    User user = new User();

    String encryptedPassword = authenticationUI.encryptPassword(password);

    user.setUsername(username);
    
    user.setPassword(encryptedPassword);
    user.setEmailAddress(email);
    
    accountManager.register(user); 
    
    login();
    return ""; // return "pretty:user"
  }

  public void validateUsername(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    if (accountManager.isUsernameTaken((String) value))
      throw new ValidatorException(
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "This username is already taken.", "'" + value + "' is taken"));
  }
  
  private void login() {
    authenticationUI.login(username, password);
  }
}
