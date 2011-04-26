Sample web service clients for device detection service (DeDS).

DeDS provides a service to detect mobile devices and its capabilities by passing
http headers, in particular the 'user-agent' header.  There are other http 
headers that mobile devices set, however the 'User-Agent' is the only required 
header.  The client does not need to send all http headers, there 
are some headers that maybe sensitive due to security.  Some SSO solutions use 
proprietary headers.  The more headers is sent over, the better chances of
detecting the device.  Headers such as but not limited to: UA-Pixels, 
X-OperaMini-Phone-UA, Accept helps in making better device detection in the
future.

The web service uses just plain http get and json for encoding object/data.
This should be simple and straight forward for most languages and platforms.  
It uses mainly the wurfl open source project to do the detection.  

[AVAILABLE METHODS]

- get_capabilities - 
Returns a value for a particular capability of the device. Pass in many
capability, service will return a map of where capability will be the key
and information of the device on the value.

parameters:
header - hash map of http header key value pairs that is json encoded
capability - string name of the capability key (ex. resolution_width).  
1 or more capability http param is allowed
For a complete name of capability keys wurfl docs:  
http://wurfl.sourceforge.net/help_doc.php

url:
[dds_url]/get_capabilities?header=[json encoded header]&capability=[capability key/name]

- get_statusinfo - 
Returns an object of the status of the webservice.  status info
has some useful attributes such as when the data of the devices was last 
modified this would allow comparison of cached device info objects.

parameters:
none

url:
[dds_url]/get_statusinfo

[SAMPLE CLIENTS]

- java - 
A sample servlet and is a maven project

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


