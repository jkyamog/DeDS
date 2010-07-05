<?php
/*
 * Copyright (C) 2010 Catalyst IT Limited
 *
 * Copying and distribution of this file, with or without modification,
 * are permitted in any medium without royalty provided the copyright
 * notice and this notice are preserved.  This file is offered as-is,
 * without any warranty.
 */

define("DDS_WS_URL", 'http://localhost:8080/dds/services');
header('Content-type: text/plain');

$headers = getallheaders();
$json_headers = json_encode($headers);

// get one capability, the screen size width
$url = DDS_WS_URL.'/get_capability?capability=resolution_width&headers='.urlencode($json_headers);
$ch = curl_init($url);
curl_setopt($ch, CURLOPT_TIMEOUT_MS, 2000);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$json_response = curl_exec($ch);
curl_close($ch);
echo 'resolution_width = '.json_decode($json_response);

// get device info object, where it contains most used capabilities
$url = DDS_WS_URL.'/get_deviceinfo?headers='.urlencode($json_headers);
$ch = curl_init($url);
curl_setopt($ch, CURLOPT_TIMEOUT_MS, 2000);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$json_response = curl_exec($ch);
curl_close($ch);
echo "\ndevice info = ";
var_dump(json_decode($json_response));


?>
