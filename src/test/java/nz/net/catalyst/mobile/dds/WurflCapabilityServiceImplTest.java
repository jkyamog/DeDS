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

package nz.net.catalyst.mobile.dds;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;
import org.slf4j.Logger;

public class WurflCapabilityServiceImplTest {
   
   @Mocked
   Logger logger;
   
   @Test
   public void testInit() throws IOException {
      WurflCapabilityServiceImpl wurflCS = new WurflCapabilityServiceImpl();

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
   public void testReloadAndCleanup() throws InterruptedException {
      final WurflCapabilityServiceImpl wurflCS = new WurflCapabilityServiceImpl();
      final String wurflDirPath = "src/main/webapp/WEB-INF/wurfl";
      final File wurflDir = new File(wurflDirPath);

      new Expectations() {
         {
            setField(wurflCS, "logger", logger);

            // these are the correct logs that should happen, note there are some concurrency happening
            // so the test below added some sleep for the proper events to happen in
            // consistent sequence
            logger.info("search for wurfl file and patches on: " + wurflDir.getAbsolutePath());
            logger.debug("wurfl file: " + wurflDir.getAbsolutePath() + "/wurfl.xml");
            logger.debug("wurfl patch file: " + wurflDir.getAbsolutePath() + "/wurfl_patch.xml");
            logger.debug("wurfl patch file: " + wurflDir.getAbsolutePath() + "/web_browsers_patch.xml");
            logger.debug("watching " + wurflDir.getAbsolutePath() + "/wurfl.xml");
            logger.debug("watching " + wurflDir.getAbsolutePath() + "/wurfl_patch.xml");
            logger.debug("watching " + wurflDir.getAbsolutePath() + "/web_browsers_patch.xml");
            logger.info(wurflDir.getAbsolutePath() + "/wurfl.xml file changed, reloading");
            logger.info(wurflDir.getAbsolutePath() + "/wurfl_patch.xml file changed, reloading");
            logger.warn("unable to obtain wurflReloadLock another thread be in the process of reloading, not reloading");
            logger.info("reload successful");
            logger.info("stopping watching wurfl");
         }
      };
      
      wurflCS.setWurflDirPath(wurflDirPath);
      wurflCS.init();
      
      class ReloadThread extends Thread {

         @Override
         public void run() {
            try {
               System.out.println("waiting for other wurfl service to shutdown");
               sleep(3000);

               System.out.println("touching wurfl.xml");
               File wurflFile = new File("src/main/webapp/WEB-INF/wurfl/wurfl.xml");
               wurflFile.setLastModified((new Date()).getTime());
            
               sleep(3000);
               // wait until the fam detects wurfl.xml is changed, otherwise would be hard to
               // determine which file gets detected first.  testing result will not be consistent
               
               // try to reload again while another reload is in progress, by changing the patch file
               System.out.println("touching wurfl_patch.xml");
               File wurflFilePatch = new File("src/main/webapp/WEB-INF/wurfl/wurfl_patch.xml");
               wurflFilePatch.setLastModified((new Date()).getTime()); 
               
               // need to wait for reload to finish
               System.out.println("waiting for reload to finish...");
               sleep(10000); 
               
               wurflCS.cleanup();
               System.out.println("waiting for shutdown");
               sleep(5000); // wait for shutdown

            } catch (InterruptedException e) {
               fail("not expecting an exception");
            } 
         }
         
      };

      ReloadThread rt = new ReloadThread();
      rt.start();
      rt.join();
   }

}
