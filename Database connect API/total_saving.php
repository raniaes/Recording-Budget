<?php
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
        
    $statement = mysqli_prepare($con, "SELECT price, chose FROM money WHERE id = ?"); 
    mysqli_stmt_bind_param($statement, "s", $id);
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
    
    echo json_encode($response); // 더한 값 출력

?>