<?php
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
       
    $statement = mysqli_prepare($con, "SELECT code, name, admin FROM joint_table WHERE id = ?"); 
    mysqli_stmt_bind_param($statement, "s", $id);
    mysqli_stmt_execute($statement);

    $result = mysqli_stmt_get_result($statement);

    $count = 0;
    $response = array();
    $response["success"] = false;

    while($row = mysqli_fetch_assoc($result)) {
        $response["success"] = true;
        $count += 1;
        $response["code" . $count] = $row['code'];
        $response["name" . $count] = $row['name'];
        $response["admin" . $count] = $row['admin'];
    }
    $response["count"] = $count;
    
    echo json_encode($response); // 더한 값 출력

?>