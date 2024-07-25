<?php
//path
$uploadDir = "draw/";

$imageData = $_POST['image'];
$fileName = $_POST['filename'];

// decode
$imageData = str_replace('data:image/jpeg;base64,', '', $imageData);
$imageData = str_replace(' ', '+', $imageData);
$imageData = base64_decode($imageData);

// Filename
$fileName = $fileName . '.jpg';

// FilePath
$filePath = $uploadDir . $fileName;

// File save
if (file_put_contents($filePath, $imageData)) {
    echo "success";
} else {
    echo "error";
}
?>