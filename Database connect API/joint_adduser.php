<?php 
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $name = isset($_POST["name"]) ? $_POST["name"] : "";
    $admin = 0;
    $code = isset($_POST["code"]) ? $_POST["code"] : "";

    $statement = mysqli_prepare($con, "SELECT * FROM budget WHERE id = ?");
    mysqli_stmt_bind_param($statement, "s", $id);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);

    $response = array();
    $response["success"] = "0";

    if (mysqli_stmt_fetch($statement)){
        $statement2 = mysqli_prepare($con, "SELECT * FROM joint_table WHERE code = ? AND name = ? AND admin = ? AND id = ?");
        mysqli_stmt_bind_param($statement2, "ssis", $code, $name, $admin, $id);
        mysqli_stmt_execute($statement2);
        mysqli_stmt_store_result($statement2);
        $response["success"] = "1";
        
        if (mysqli_stmt_num_rows($statement2) == 0){
            $statement3 = mysqli_prepare($con, "INSERT INTO joint_table VALUES (?,?,?,?)");
            mysqli_stmt_bind_param($statement3, "ssis", $code, $name, $admin, $id);
            if (mysqli_stmt_execute($statement3)) {
            } else {
                echo "Error: " . mysqli_error($con);
            }
            $response["success"] = "2";
        } 
    }

    mysqli_stmt_free_result($statement);
   
    echo json_encode($response);
?>