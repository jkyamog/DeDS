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
http://git.catalyst.net.nz/gitweb?p=dds.git;a=blob_plain;f=main/dist/dds.war;hb=HEAD

2. Deploy dds.war file into your application server
on tomcat this is simply copiying dds.war file into the webapps dir
dds.war was tested on java 1.6 and tomcat 5.5/6.  You may need to modify 
WEB-INF/classes/log4j.properties if your app server is not tomcat and set
the correct path for dds.log

3. Visit the status page on 
http://yourhost/dds/services/status_page

[UPDATE WURFL FILE AND PATCHES]

Simply copy the main wurfl file to WEB-INF/wurfl/wurfl.xml

Any other .xml files on WEB-INF/wurfl dir are treated as wurfl patches

dds will monitor files on WEB-INF/wurfl, any file modified will trigger a reload
of wurfl

[HOW TO USE THE WEBSERVICE]

Look into README.TXT on sample_clients dir or the link below

http://git.catalyst.net.nz/gitweb?p=dds.git;a=blob_plain;f=sample_clients/README.TXT;hb=HEAD

There are also sample clients below

http://git.catalyst.net.nz/gitweb?p=dds.git;a=tree;f=sample_clients;hb=HEAD

[GET THE SOURCE CODE]
- git -

git clone http://git.catalyst.net.nz/dds.git

- download latest tarball -

http://git.catalyst.net.nz/gitweb?p=dds.git;a=snapshot;h=HEAD;sf=tgz

[BUILD THE CODE]

The source tree itself can be imported to eclipse as a java project or

cd main
ant

Should build a new dds.war

[CONTACT]
Jun Yamog - jun@catalyst.net.nz
