<?php
/*
 * Copyright (C) 2010 Catalyst IT Limited
 *
 * Copying and distribution of this file, with or without modification,
 * are permitted in any medium without royalty provided the copyright
 * notice and this notice are preserved.  This file is offered as-is,
 * without any warranty.
 */

define("DDS_WS_URL", 'http://deds.jkyamog.cloudbees.net/services/v1');
header('Content-type: text/plain');

$headers = getallheaders();
$json_headers = json_encode($headers);

// get the capabilities
$url = DDS_WS_URL.'/get_capabilities?capability=resolution_width&&capability=model_name&capability=xhtml_support_level&headers='.urlencode($json_headers);
$ch = curl_init($url);
curl_setopt($ch, CURLOPT_USERAGENT, 'curl');
curl_setopt($ch, CURLOPT_TIMEOUT_MS, 2000);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$json_response = curl_exec($ch);
curl_close($ch);
echo "\ncapabilities = ";
var_dump(json_decode($json_response));


?>
