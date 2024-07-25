<?php
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $code = isset($_POST["code"]) ? $_POST["code"] : "";
    $admin = 0;
       
    $statement = mysqli_prepare($con, "SELECT id FROM joint_table WHERE code = ? AND admin = ?"); 
    mysqli_stmt_bind_param($statement, "si", $code, $admin);
    mysqli_stmt_execute($statement);

    $result = mysqli_stmt_get_result($statement);

    $count = 0;
    $response = array();
    $response["success"] = false;

    while($row = mysqli_fetch_assoc($result)) {
        $response["success"] = true;
        $count += 1;
        $response["id" . $count] = $row['id'];
    }
    $response["count"] = $count;
    
    echo json_encode($response); // 더한 값 출력

?>