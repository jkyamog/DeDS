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

$dds_url = 'http://deds.jkyamog.cloudbees.net/services/v1';

$json = JSON->new->allow_nonref;

$headers = {};
$headers->{'User-Agent'} = 'Mozilla/5.0 (SymbianOS/9.3; U; Series60/3.2 NokiaE75-1/110.48.125 Profile/MIDP-2.1 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413';

$encoded_header = $json->encode($headers);

# get the capabilities
$json_response = get $dds_url.'/get_capabilities?headers='.$encoded_header.'&capability=resolution_width&capability=model_name&capability=xhtml_support_level';

# get device info object, where it contains most used capabilities
$capabilities = $json->decode($json_response);
print Dumper $capabilities;

