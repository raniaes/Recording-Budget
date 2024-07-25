<?php
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $startDate = isset($_POST["startDate"]) ? $_POST["startDate"] : "";
    if ($startDate != ""){
        $dateTime = new DateTime($startDate);
        $startformat = $dateTime -> format('Y-m-d H:i:s');
    }
    $endDate = isset($_POST["endDate"]) ? $_POST["endDate"] : "";
    if ($endDate != ""){
        $dateTime = new DateTime($endDate);
        $dateTime->setTime(23, 59, 59);
        $endformat = $dateTime->format('Y-m-d H:i:s');
    }
       
    $statement = mysqli_prepare($con, "SELECT price, chose FROM money WHERE id = ? AND date > ? AND date < ?"); 
    mysqli_stmt_bind_param($statement, "sss", $id, $startformat, $endformat);
    mysqli_stmt_execute($statement);

    $result = mysqli_stmt_get_result($statement);

    $response = array();
    $response["success"] = false;
    $response["sum_true"] = 0;
    $response["sum_false"] = 0;
    $response["left"] = 0;

    while($row = mysqli_fetch_assoc($result)) {
        $response["success"] = true;
        if ($row['chose'] == true) {
            $response["sum_true"] += $row['price'];
        } else {
            $response["sum_false"] += $row['price'];
        }       
    }

    if ($response["sum_true"] > $response["sum_false"]){
        $response["left"] = $response["sum_true"] - $response["sum_false"];
    }else {
        $response["left"] = 0;
    }
    
    
    echo json_encode($response); // 더한 값 출력

?>