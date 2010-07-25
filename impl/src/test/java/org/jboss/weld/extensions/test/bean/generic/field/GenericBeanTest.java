/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.weld.extensions.test.bean.generic.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class GenericBeanTest
{
   @Deployment
   public static Archive<?> deploy()
   {
      return ShrinkWrap.create("test.jar", JavaArchive.class).addPackage(GenericBeanTest.class.getPackage());
   }

   @Inject
   @Foo(1)
   private Baz baz1;

   @Inject
   @Foo(2)
   private Baz baz2;
   
   @Inject
   @Foo(1)
   private Bar bar1;
   
   @Inject
   @Foo(2)
   private Bar bar2;
   
   @Inject @Qux @Foo(1)
   private String bar1Message;
   
   @Inject @Qux @Foo(2)
   private String bar2Message;
   
   @Inject @Foo(1)
   private Message baz1Message;
   
   @Inject @Foo(2)
   private Message baz2Message;

   @Test
   public void testGeneric()
   {
      // Check that normal bean injection is working correctly!
      assertNotNull(baz2.getCorge());
      assertEquals(baz2.getCorge().getName(), "fred");
      
      // Test that the generic configuration injection wiring is working for bar
      assertNotNull(bar1.getInjectedMessage());
      assertEquals(bar1.getInjectedMessage().value(), "bye1");
      assertNotNull(bar2.getInjectedMessage());
      assertEquals(bar2.getInjectedMessage().value(), "bye2");
      
      // Check that the generic configuration injection wiring is working for baz
      assertNotNull(baz1.getMessage());
      assertEquals(baz1.getMessage().value(), "hello1");
      assertNotNull(baz2.getMessage());
      assertEquals(baz2.getMessage().value(), "hello2");
      
      // Check that this isn't affecting annotations on the generic bean without @Inject 
      assertNull(baz1.getBar().getMessage());
      assertNull(baz2.getBar().getMessage());
      
      // Check that producer methods on generic beans are working
      assertNotNull(bar1Message);
      assertEquals(bar1Message, "barbye1");
      assertNotNull(bar2Message);
      assertEquals(bar2Message, "barbye2");
      
      assertNotNull(baz1Message);
      assertEquals(baz1Message.value(), "hello1");
      assertNotNull(baz2Message);
      assertEquals(baz2Message.value(), "hello2");
     
   }
}