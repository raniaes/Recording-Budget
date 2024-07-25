<?php 
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $password = isset($_POST["password"]) ? $_POST["password"] : "";

    $statement = mysqli_prepare($con, "UPDATE budget SET password=? WHERE id=?");
    mysqli_stmt_bind_param($statement, "ss", $password, $id);
    mysqli_stmt_execute($statement);


    $response = array();
    if(!$statement){
        $response["success"] = false;
    }else{
        $response["success"] = true;
    }


    
    echo json_encode($response);
?>