<?php 
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $chose = isset($_POST["chose"]) ? $_POST["chose"] : "";
    $date = isset($_POST["date"]) ? $_POST["date"] : "";
    $name = isset($_POST["name"]) ? $_POST["name"] : "";
    $re_name = isset($_POST["re_name"]) ? $_POST["re_name"] : "";
    $price = isset($_POST["price"]) ? $_POST["price"] : "";
    $picture = isset($_POST["picture"]) ? $_POST["picture"] : null;

    if ($picture === "nul"){
        $picture = null;
    }

    $statement = mysqli_prepare($con, "UPDATE money SET name=?, chose=?, price=?, picture=? WHERE id=? AND date=? AND name=?");
    mysqli_stmt_bind_param($statement, "siissss", $re_name, $chose, $price, $picture, $id, $date, $name);
    mysqli_stmt_execute($statement);


    $response = array();
    if(!$statement){
        $response["success"] = false;
    }else{
        $response["success"] = true;
    }


    
    echo json_encode($response);
?>