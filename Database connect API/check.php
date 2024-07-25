<?php
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    
    $statement = mysqli_prepare($con, "SELECT * FROM budget WHERE id = ?");
    mysqli_stmt_bind_param($statement, "s", $id);
    mysqli_stmt_execute($statement);


    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $id, $password, $name, $question, $answer, $target, $price, $date);

    $response = array();
    $response["success"] = false;
 
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["id"] = $id;
        $response["question"] = $question;
        $response["answer"] = $answer;
    }

    $statement2 = mysqli_prepare($con, "SELECT * FROM joint_table WHERE code = ?");
    mysqli_stmt_bind_param($statement2, "s", $id);
    mysqli_stmt_execute($statement2);

    mysqli_stmt_store_result($statement2);
    mysqli_stmt_bind_result($statement2, $code, $name, $admin, $id);

    while(mysqli_stmt_fetch($statement2)) {
        $response["success"] = true;
    }

    echo json_encode($response);

?>