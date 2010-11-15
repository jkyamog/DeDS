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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * Serialize and deserialize objects using JSON and jackson project implementation
 * 
 * @author jun yamog
 *
 */

public class JacksonCapabilityServiceDeSerializerImpl
      implements CapabiltyServiceDeSerializer {

   private ObjectMapper mapper = new ObjectMapper();
   
   private final static Log logger = LogFactory.getLog(JacksonCapabilityServiceDeSerializerImpl.class);

   @Override
   public Map<String, String> deserializeHeaders(InputStream inputStream) 
      throws IOException, ParseException {
      
      try {
         Map<String, String> headers = mapper.readValue(inputStream, new TypeReference<Map<String, String>>() { });
         return headers;
         
      } catch (JsonParseException e) {
         logger.warn("unable to parse headers", e);
         throw new ParseException(e.getMessage(),0);
      } catch (JsonMappingException e) {
         logger.warn("unable to parse headers", e);
         throw new ParseException(e.getMessage(),0);
      }
   }

   @Override
   public void serializeDeviceInfo(DeviceInfo deviceInfo, OutputStream outputStream) 
      throws IOException, ParseException {

      serializeObject(deviceInfo, outputStream);
   }

   @Override
   public void serializeCapability(Object capability, OutputStream outputStream)
         throws IOException, ParseException {

      serializeObject(capability, outputStream);
/*      
      try {
         // it may seem silly to write the value straight, however it is a valid
         // json notation.  This leaves the other side to decode and decide
         // the object type based on the value
         IOUtils.write(capability.toString(), outputStream);
      } catch (JsonGenerationException e) {
         logger.warn("unable to serialize capability", e);
         throw new ParseException(e.getMessage(),0);
      } catch (JsonMappingException e) {
         logger.warn("unable to serialize capability", e);
         throw new ParseException(e.getMessage(),0);
      }
*/      
   }

   @Override
   public void serializeStatusInfo(StatusInfo statusInfo, OutputStream outputStream) 
      throws IOException, ParseException {
      
      serializeObject(statusInfo, outputStream);

   }
   
   private void serializeObject(Object object, OutputStream outputStream)       
      throws IOException, ParseException {

      try {
         mapper.writeValue(outputStream, object);
      } catch (JsonGenerationException e) {
         logger.warn("unable to serialize", e);
         throw new ParseException(e.getMessage(),0);
      } catch (JsonMappingException e) {
         logger.warn("unable to serialize", e);
         throw new ParseException(e.getMessage(),0);
      }


   }

}
