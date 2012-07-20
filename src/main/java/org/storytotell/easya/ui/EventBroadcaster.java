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

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.storytotell.easya.annotations.Logged;
import org.storytotell.easya.events.CourseDeleted;
import org.storytotell.easya.events.HasFacesMessage;

/**
 * Catches some interesting events and sends them to the user.
 * 
 * @author Daniel Lyons <fusion@storytotell.org>
 */
@Named
@RequestScoped
@Logged
public class EventBroadcaster {
  /**
   * Catch a delete course event.
   * @param deleteEvent 
   */
  public void onCourseDeleted(@Observes CourseDeleted deleteEvent) {
    broadcast(deleteEvent);
  }
  
  /**
   * Send something with a FacesMessage on to JSF.
   * @param obj the object that can be converted
   */
  public void broadcast(HasFacesMessage obj) {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.addMessage(null, obj.getFacesMessage());
  }
}
