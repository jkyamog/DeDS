#!/usr/bin/perl

use JSON;
use Data::Dumper;
use LWP::Simple;

$json = JSON->new->allow_nonref;

$headers = {};
$headers->{'User-Agent'} = 'Mozilla/5.0 (SymbianOS/9.3; U; Series60/3.2 NokiaE75-1/110.48.125 Profile/MIDP-2.1 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413';

$encoded_header = $json->encode($headers);

$encoded_deviceinfo = get 'http://localhost:8080/mobileweb/app/dds/get_deviceinfo?headers='.$encoded_header;
$deviceinfo = $json->decode($encoded_deviceinfo);

print Dumper $deviceinfo;

$max_image_width = $json->decode(get 'http://localhost:8080/mobileweb/app/dds/get_capability?headers='.$encoded_header.'&capability=max_image_width');

print "width = $max_image_width\n";
