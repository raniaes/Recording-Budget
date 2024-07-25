<?php 
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $code = isset($_POST["code"]) ? $_POST["code"] : "";


    $statement = mysqli_prepare($con, "DELETE FROM joint_table WHERE code=? AND id=?");
    mysqli_stmt_bind_param($statement, "ss", $code, $id);
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