Device Detection Service for Mobile Phone/Devices

This is a simple webservice to do Device Detection for Mobile Phone/Devices.  
This is mainly based from wurfl project (http://wurfl.sourceforge.net/).  
This just exposes wurfl as a simple http json api.

This makes it easy for you get up and running.  Wurfl files can be placed a 
common place, making it easy to manage wurfl patch files.  Since this is 
just a web service, other languages can now easily leverage the wurfl api and 
data.

[HOW TO INSTALL]

1. Download the latest dds.war file from

https://github.com/jkyamog/DeDS/archives/master

2. Deploy dds.war file into your application server
on tomcat this is simply copying dds.war file into the webapps dir.
dds.war was tested on java 1.6 and tomcat 5.5/6.  You may need to modify 
WEB-INF/classes/log4j.properties if your app server is not tomcat and set
the correct path for dds.log

3. Visit the status page on 
http://yourhost/dds/services/v1/status_page

[UPDATE WURFL FILE AND PATCHES]

Simply copy the main wurfl file to WEB-INF/wurfl/wurfl.xml

Any other .xml files on WEB-INF/wurfl dir are treated as wurfl patches

dds will monitor files on WEB-INF/wurfl, any file modified will trigger a reload
of wurfl

[HOW TO USE THE WEBSERVICE]

Look into README.TXT on sample_clients dir or the link below

https://github.com/jkyamog/DeDS/tree/master/sample_clients

[DEMO SERVICE]

An instance of DeDS is running on Cloud Bees.  The sample clients are pointed to the
url below.

http://deds.jkyamog.cloudbees.net/services/v1

[GET THE SOURCE CODE]
- git -

git clone git://github.com/jkyamog/DeDS.git

[BUILD THE CODE]

DeDS is a maven project.  So you will need to invoke the following.

mvn clean package

If you are interested to use the java code directly, its also available on maven
central. Just add the dependency below:

<dependency>
  <groupId>nz.net.catalyst</groupId>
  <artifactId>mobile-dds</artifactId>
  <version>1.1.1</version>
</dependency>

[CONTACT]
Jun Yamog - jun@catalyst.net.nz
