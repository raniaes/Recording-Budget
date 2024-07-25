<?php 
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $question = isset($_POST["question"]) ? $_POST["question"] : "";
    $answer = isset($_POST["answer"]) ? $_POST["answer"] : "";

    $statement = mysqli_prepare($con, "UPDATE budget SET question=?, answer=? WHERE id=?");
    mysqli_stmt_bind_param($statement, "sss", $question, $answer, $id);
    mysqli_stmt_execute($statement);


    $response = array();
    if(!$statement){
        $response["success"] = false;
    }else{
        $response["success"] = true;
    }


    
    echo json_encode($response);
?>