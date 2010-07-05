Sample web service clients for device detection service (DDS).  

DDS provides a service to detect mobile devices and its capabilities by passing 
http headers, in particular the 'user-agent' header.  There are other http 
headers that mobile devices set, however the 'User-Agent' is the only required 
header.  The client does not need to send all http headers, there 
are some headers that maybe sensitive due to security.  Some SSO solutions use 
proprietary headers.  The more headers is sent over, the better chances of
detecting the device.  Headers such as but not limited to: UA-Pixels, 
X-OperaMini-Phone-UA, Accept helps in making better device detection.

The web service uses just plain http get and json for encoding object/data.
This should be simple and straight forward for most languages and platforms.  
It uses mainly the wurfl open source project to do the detection.  

[AVAILABLE METHODS]

- get_deviceinfo - 
Returns a device info object that has capabilities that is mostly
use for development.  This is the best method to use, as it reduces back and 
forth traffic through the web service.  1 service call give you about 10 most 
used capabilities.

parameters:
header - hash map of http header key value pairs that is json encoded

url:
dds_url/get_deviceinfo?header=[json encoded header]

- get_capability - 
Returns a value for a particular capability of the device, normally
used when device info does not have the needed capability.  1 service call give 
you only 1 capability.  If such a capability becomes popular for develoment, 
its likely it will be availble on get_deviceinfo eventually.

parameters:
header - hash map of http header key value pairs that is json encoded
capability - string name of the capability key (ex. resolution_width).  For a 
complete name of capability keys wurfl docs:  
http://wurfl.sourceforge.net/help_doc.php

url:
dds_url/get_capability?header=[json encoded header]&capability=[capability key/name]

- get_statusinfo - 
Returns an object of the status of the webservice.  status info
has some useful attributes such as when the data of the devices was last 
modified this would allow comparison of cached device info objects.

parameters:
none

url:
dds_url/get_statusinfo

[SAMPLE CLIENTS]

- java - 
under 'sampledds' directory there is a sample servlet, a user can also import 
this directory into eclipse as a project.

1. copy sampledds.war into your webapps folder.  
2. Then visit the url "http://yourhost/sampledds/"

You can change the dds url on web.xml

tested on: java 1.6 and tomcat 6.0, may run on java
1.5 and servlet 2.4

- php -
a single web php file.

1. copy sampleclient.php to your webserver that has php enabled.  
2. Then visit the "http://yourhost/sampleclient.php" page.

You can change the dds url on constant on the file

requires: php curl and php > 5.2.0

- perl - 
a single shell script.

1. run the shell script from the command line "./sampleclient.pl"

requires: JSON and LWP cpan modules


