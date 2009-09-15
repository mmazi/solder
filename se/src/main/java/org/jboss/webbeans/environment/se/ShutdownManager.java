/**
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.webbeans.environment.se;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;

import javax.inject.Inject;
import org.jboss.webbeans.bootstrap.api.Bootstrap;
import org.jboss.webbeans.environment.se.events.Shutdown;
import org.jboss.webbeans.log.LogProvider;
import org.jboss.webbeans.log.Logging;
import org.jboss.webbeans.manager.api.WebBeansManager;

@ApplicationScoped
public class ShutdownManager
{
   
   private static LogProvider log = Logging.getLogProvider(ShutdownManager.class);
   private @Inject WebBeansManager manager;

   private boolean hasShutdownBeenCalled = false;
   
   private Bootstrap bootstrap;
   
   /**
    * The observer of the optional shutdown request which will in turn fire the
    * Shutdown event.
    * 
    * @param shutdownRequest
    */
   public void shutdown(@Observes @Shutdown BeanManager shutdownRequest)
   {
      synchronized (this)
      {
         
         if (!hasShutdownBeenCalled)
         {
            hasShutdownBeenCalled = true;
            bootstrap.shutdown();
         }
         else
         {
            log.debug("Skipping spurious call to shutdown");
            log.trace(Thread.currentThread().getStackTrace());
         }
      }
   }

   /**
    * Shutdown WebBeans SE gracefully (call this as an alternative to firing the
    * "@Shutdown Manager" event.
    */
   public void shutdown() {
       shutdown(manager);
   }
   
   public void setBootstrap(Bootstrap bootstrap)
   {
      this.bootstrap = bootstrap;
   }
   
}
