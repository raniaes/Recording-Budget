<?php
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $name = isset($_POST["name"]) ? $_POST["name"] : "";
    
    $statement = mysqli_prepare($con, "SELECT * FROM budget WHERE id = ? AND name = ?");
    mysqli_stmt_bind_param($statement, "ss", $id, $name);
    mysqli_stmt_execute($statement);


    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $id, $password, $name, $question, $answer, $target, $price, $date);

    $response = array();
    $response["success"] = false;
 
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["id"] = $id;
        $response["password"] = $password;
        $response["name"] = $name;
        $response["question"] = $question;      
        $response["answer"] = $answer;
        $response["target"] = $target;
        $response["price"] = $price;
        $response["date"] = $date;
    }

    echo json_encode($response);

?>