<?php

$filename = $_GET['filename'];

$imagePath = 'draw/' . $filename . '.jpg'; 

header('Content-Type: image/jpeg');
readfile($imagePath);
?>