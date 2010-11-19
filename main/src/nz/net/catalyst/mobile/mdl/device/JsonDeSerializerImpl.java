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

public class JsonDeSerializerImpl
      implements ObjectDeSerializer {

   private static ObjectMapper mapper = new ObjectMapper();
   
   private final static Log logger = LogFactory.getLog(JsonDeSerializerImpl.class);

   @Override
   public <T> T deserialize(InputStream inputStream) 
      throws IOException, ParseException {
      
      try {
         return JsonDeSerializerImpl.mapper.<T>readValue(inputStream, new TypeReference<T>() {});
         
      } catch (JsonParseException e) {
         logger.warn("unable to parse headers", e);
         throw new ParseException(e.getMessage(),0);
      } catch (JsonMappingException e) {
         logger.warn("unable to parse headers", e);
         throw new ParseException(e.getMessage(),0);
      }
   }

   public void serialize(Object object, OutputStream outputStream)       
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
