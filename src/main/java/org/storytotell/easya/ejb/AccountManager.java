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
package org.storytotell.easya.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.storytotell.easya.entity.User;
import org.storytotell.easya.events.AccountRegistered;

/**
 * Handles User lookup and registration.
 * 
 * @author Daniel Lyons <fusion@storytotell.org>
 */
@Stateless
@LocalBean
@Named("AccountManager")
public class AccountManager {
  private static final Logger log = LoggerFactory.getLogger(AccountManager.class);
  private @PersistenceContext EntityManager em;

  private @Inject Event<AccountRegistered> accountRegistered;

  public User findByUsername(String username) {
    return em.find(User.class, username);
  }
  
  public boolean isUsernameTaken(String username) {
    log.debug("verifying availability of {}", username);
    
    String query = "SELECT COUNT(u) FROM User u WHERE u.username = :username";
    
    long count = (Long)em.createQuery(query)
            .setParameter("username", username)
            .getSingleResult();
    
    return count == 1;
  }
  
  public void register(User user) {
    log.debug("registering {}", user.getUsername());
    
    em.persist(user);
    accountRegistered.fire(new AccountRegistered(user));
  }
}
