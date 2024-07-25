<?php 
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $target = isset($_POST["target"]) ? $_POST["target"] : "";
    $price = isset($_POST["price"]) ? $_POST["price"] : "";
    $date = isset($_POST["date"]) ? $_POST["date"] : "";

    $statement = mysqli_prepare($con, "UPDATE budget SET target=?, price=?, date=? WHERE id=?");
    mysqli_stmt_bind_param($statement, "siss", $target, $price, $date, $id);
    mysqli_stmt_execute($statement);


    $response = array();
    if(!$statement){
        $response["success"] = false;
    }else{
        $response["success"] = true;
    }


    
    echo json_encode($response);
?>