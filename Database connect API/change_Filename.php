<?php
$filename = isset($_POST["filename"]) ? $_POST["filename"] : "";
$re_filename = isset($_POST["re_filename"]) ? $_POST["re_filename"] : "";

$filePath = 'draw/'.$filename.'.jpg';
$newFileName = 'draw/'.$re_filename.'.jpg';         


if (file_exists($filePath)) {
    if (rename($filePath, $newFileName)) {
        echo 'success';
    } else {
        echo 'fail';
    }
} else {
    echo 'does not exist';
}
?>