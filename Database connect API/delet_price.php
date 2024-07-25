<?php 
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $date = isset($_POST["date"]) ? $_POST["date"] : "";
    $name = isset($_POST["name"]) ? $_POST["name"] : "";

   $statement = mysqli_prepare($con, "DELETE FROM money WHERE id=? AND date=? AND name=?");
    mysqli_stmt_bind_param($statement, "sss", $id, $date, $name);
    mysqli_stmt_execute($statement);


    $response = array();
    if(!$statement){
        $response["success"] = false;
    }else{
        $response["success"] = true;
    }

    $response["date"] = $date;


    
    echo json_encode($response);
?>