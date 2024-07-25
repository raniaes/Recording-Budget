<?php
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";

    $statement = mysqli_prepare($con, "SELECT target, price, date FROM budget WHERE id = ?"); 
    mysqli_stmt_bind_param($statement, "s", $id);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $target, $price, $date);
    mysqli_stmt_fetch($statement);

    mysqli_close($con);

    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');
       
    $statement = mysqli_prepare($con, "SELECT price, chose FROM money WHERE id = ? AND date > ?"); 
    mysqli_stmt_bind_param($statement, "ss", $id, $date);
    mysqli_stmt_execute($statement);

    $result = mysqli_stmt_get_result($statement);

    $response = array();
    $response["success"] = false;
    while($row = mysqli_fetch_assoc($result)) {
        $response["success"] = true;
        if ($row['chose'] == true) {
            $response["sum_true"] += $row['price'];
        } else {
            $response["sum_false"] += $row['price'];
        }       
    }

    if ($response["sum_true"] > $response["sum_false"]){
        $response["total"] = $response["sum_true"] - $response["sum_false"];
    }else {
        $response["total"] = 0;
    }

    $response["target"] = $target;
    $response["price"] = $price;

    if ($response["price"] > $response["total"]){
        $response["left"] = number_format(($response["total"]/$response["price"]) * 100, 1);
    }else{
        $response["left"] = 100;
    }
    
    
    echo json_encode($response); // 더한 값 출력

?>