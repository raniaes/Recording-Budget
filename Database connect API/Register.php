<?php 
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $password = isset($_POST["password"]) ? $_POST["password"] : "";
    $name = isset($_POST["name"]) ? $_POST["name"] : "";
    $question = isset($_POST["question"]) ? $_POST["question"] : "";
    $answer = isset($_POST["answer"]) ? $_POST["answer"] : "";
    $target = isset($_POST["target"]) ? $_POST["target"] : NULL;
    $price = isset($_POST["price"]) ? $_POST["price"] : NULL;
    $date = isset($_POST["date"]) ? $_POST["date"] : NULL;

    $statement = mysqli_prepare($con, "INSERT INTO budget VALUES (?,?,?,?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "ssssssis", $id, $password, $name, $question, $answer, $target, $price, $date);
    mysqli_stmt_execute($statement);


    $response = array();
    if(!$statement){
        $response["success"] = false;
    }else{
        $response["success"] = true;
    }
    
 
   
    echo json_encode($response);
?>