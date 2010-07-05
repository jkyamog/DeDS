#!/usr/bin/perl

# Copyright (C) 2010 Catalyst IT Limited
#
# Copying and distribution of this file, with or without modification,
# are permitted in any medium without royalty provided the copyright
# notice and this notice are preserved.  This file is offered as-is,
# without any warranty.


use JSON;
use Data::Dumper;
use LWP::Simple;

$dds_url = 'http://localhost:8080/dds/services';

$json = JSON->new->allow_nonref;

$headers = {};
$headers->{'User-Agent'} = 'Mozilla/5.0 (SymbianOS/9.3; U; Series60/3.2 NokiaE75-1/110.48.125 Profile/MIDP-2.1 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413';

$encoded_header = $json->encode($headers);

# get one capability, the screen size width
$resolution_width = $json->decode(get $dds_url.'/get_capability?headers='.$encoded_header.'&capability=resolution_width');
print "resolution_width = $resolution_width\n";

# get device info object, where it contains most used capabilities
$encoded_deviceinfo = get $dds_url.'/get_deviceinfo?headers='.$encoded_header;
$deviceinfo = $json->decode($encoded_deviceinfo);
print Dumper $deviceinfo;

