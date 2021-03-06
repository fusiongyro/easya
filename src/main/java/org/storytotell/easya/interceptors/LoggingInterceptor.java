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
package org.storytotell.easya.interceptors;

import java.io.Serializable;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.storytotell.easya.annotations.Logged;

/**
 * Implements {@code @Logged} by examining the context, finding the appropriate 
 * logger and invoking it with the arguments.
 * 
 * @author Daniel Lyons <fusion@storytotell.org>
 */
@Interceptor
@Logged
public class LoggingInterceptor implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @AroundInvoke
  public Object logMethodEntry(InvocationContext ctx) throws Exception {
    Logger log = LoggerFactory.getLogger(ctx.getTarget().getClass());
    
    // early exit if logging is disabled
    if (!log.isTraceEnabled()) return ctx.proceed();
    
    // generate the log statement
    String method = ctx.getMethod().getName();
    String arguments = parametersAsString(ctx.getParameters());
    
    log.trace("entering {}({})", method, arguments);
    try {
      Object result = ctx.proceed();
      log.trace("leaving {}({})", method, arguments);
      return result;
    } catch (Exception e) {
      log.trace("exception in {}: {}", method, e);
      throw e;
    }
  }

  /**
   * Generate the argument list for a function call based on these args.
   */
  private String parametersAsString(Object[] args) {
    StringBuilder methodArguments = new StringBuilder();

    if (args != null)
      for (Object arg : args)
        methodArguments.append(arg.toString()).append(", ");
    
    if (methodArguments.length() > 0) 
      methodArguments.delete(methodArguments.length()-2, methodArguments.length());
    
    return methodArguments.toString();
  }
}
