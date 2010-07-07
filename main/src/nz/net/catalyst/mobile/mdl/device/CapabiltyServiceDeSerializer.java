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

/**
 * Interface for client code that needs to get parameters by web services
 * 
 * @author jun yamog
 *
 */

public interface CapabiltyServiceDeSerializer {
   
   /**
    * Given an InputStream parse and deserialized the headers map.  This is essentially http headers sent by a
    * web service client to become a Map.
    * 
    * @param inputStream
    * @return
    * @throws IOException
    * @throws ParseException
    */
   public Map<String, String> deserializeHeaders(InputStream inputStream) throws IOException, ParseException;
   
   /**
    * Append to the output stream a serialized DeviceInfo bean
    * 
    * @param deviceInfo
    * @param outputStream
    * @throws IOException
    * @throws ParseException
    */
   public void serializeDeviceInfo(DeviceInfo deviceInfo, OutputStream outputStream) throws IOException, ParseException;

   /**
    * Append to the output stream a serialized capability
    * 
    * @param capability
    * @param outputStream
    * @throws IOException
    * @throws ParseException
    */
   public void serializeCapability(Object capability, OutputStream outputStream) throws IOException, ParseException;
   
   /**
    * Append to the output stream a serialized StatusInfo
    * @param statusInfo
    * @param outputStream
    * @throws IOException
    * @throws ParseException
    */
   public void serializeStatusInfo(StatusInfo statusInfo, OutputStream outputStream) throws IOException, ParseException;

}
