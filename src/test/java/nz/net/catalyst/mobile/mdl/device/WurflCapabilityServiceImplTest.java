/*
 * Copyright (C) 2010  Catalyst IT Limited
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package nz.net.catalyst.mobile.mdl.device;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class WurflCapabilityServiceImplTest {
   
   WurflCapabilityServiceImpl wurflCS;
   
   
   @Before
   public void setup() {
      wurflCS = new WurflCapabilityServiceImpl();
   }
   
   @Test
   public void testInit() throws IOException {
      try {
         wurflCS.init();
         
         fail("exception expected");
      } catch (IllegalStateException e) {
         assertEquals("wurflDirPath not properly set", e.getMessage());
      }
      
      try {
         wurflCS.setWurflDirPath("/pathdoesnotexists");
         wurflCS.init();
         
         fail("exception expected");
      } catch (IllegalArgumentException e) {
         assertEquals("wurflDirPath /pathdoesnotexists does not exists", e.getMessage());
      }
      
      File testFile = new File("/tmp/test");
      testFile.createNewFile();
      
      try {
         wurflCS.setWurflDirPath("/tmp/test");
         wurflCS.init();
         
         fail("exception expected");
      } catch (IllegalArgumentException e) {
         assertEquals("wurflDirPath /tmp/test is not a directory", e.getMessage());
      }
   }
   
   @Test
   public void testReload() {
      wurflCS.setWurflDirPath("src/main/webapp/WEB-INF/wurfl");
      wurflCS.init();
      
      File wurflFile = new File("src/main/webapp/WEB-INF/wurfl/wurfl.xml");
      wurflFile.setLastModified((new Date()).getTime());
      
   }

}
