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
import java.text.ParseException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Serialize and deserialize objects using JSON and jackson project implementation
 * 
 * @author jun yamog
 *
 */

public class JacksonJsonServiceImpl
      implements JsonService {

   private static ObjectMapper mapper = new ObjectMapper();
   
   private final Logger logger = LoggerFactory.getLogger(this.getClass());

   @Override
   public <T> T deserialize(String jsonSring) 
      throws IOException, ParseException {
      
      try {
         return JacksonJsonServiceImpl.mapper.<T>readValue(jsonSring, new TypeReference<T>() {});
         
      } catch (JsonParseException e) {
         logger.warn("unable to parse headers", e);
         throw new ParseException(e.getMessage(),0);
      } catch (JsonMappingException e) {
         logger.warn("unable to parse headers", e);
         throw new ParseException(e.getMessage(),0);
      }
   }

   @Override
   public String serialize(Object object)       
      throws IOException, ParseException {

      try {
         return mapper.writeValueAsString(object);
      } catch (JsonGenerationException e) {
         logger.warn("unable to serialize", e);
         throw new ParseException(e.getMessage(),0);
      } catch (JsonMappingException e) {
         logger.warn("unable to serialize", e);
         throw new ParseException(e.getMessage(),0);
      }


   }

}
