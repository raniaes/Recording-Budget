<?php
$filename = isset($_POST["filename"]) ? $_POST["filename"] : "";

$filePath = 'draw/'.$filename.'.jpg';

if (file_exists($filePath)) {
    if (unlink($filePath)) {
        echo 'success';
    } else {
        echo 'fail';
    }
} else {
    echo 'does not exist';
}
?>