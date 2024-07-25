<?php 
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $date = isset($_POST["date"]) ? $_POST["date"] : "";
    $name = isset($_POST["name"]) ? $_POST["name"] : "";
    $chose = isset($_POST["chose"]) ? $_POST["chose"] : "";
    $price = isset($_POST["price"]) ? $_POST["price"] : "";
    $picture = isset($_POST["picture"]) ? $_POST["picture"] : null;
    
    if ($picture === "nul"){
        $picture = null;
    }

    $statement = mysqli_prepare($con, "INSERT INTO money VALUES (?,?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "sssiis", $id, $date, $name, $chose, $price, $picture);
    mysqli_stmt_execute($statement);


    $response = array();
    if(!$statement){
        $response["success"] = false;
    }else{
        $response["success"] = true;
    }
    
 
   
    echo json_encode($response);
?>