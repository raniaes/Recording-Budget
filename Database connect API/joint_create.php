<?php 
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $name = isset($_POST["name"]) ? $_POST["name"] : "";
    $admin = 1;
    $code = substr(uniqid(), -6);

    $query = "SELECT COUNT(*) AS count FROM budget WHERE id = '$code'";
    $result = mysqli_query($con, $query);
    $row = mysqli_fetch_assoc($result);
    $count = $row['count'];

    $query = "SELECT COUNT(*) AS count FROM joint_table WHERE code = '$code'";
    $result = mysqli_query($con, $query);
    $row = mysqli_fetch_assoc($result);
    $count += $row['count'];

    while ($count > 0) {
        $code = substr(uniqid(), -6);
    
        $query = "SELECT COUNT(*) AS count FROM budget WHERE id = '$code'";
        $result = mysqli_query($con, $query);
        $row = mysqli_fetch_assoc($result);
        $count = $row['count'];

        $query = "SELECT COUNT(*) AS count FROM joint_table WHERE code = '$code'";
        $result = mysqli_query($con, $query);
        $row = mysqli_fetch_assoc($result);
        $count += $row['count'];
    }

    $statement = mysqli_prepare($con, "INSERT INTO joint_table VALUES (?,?,?,?)");
    mysqli_stmt_bind_param($statement, "ssis", $code, $name, $admin, $id);
    mysqli_stmt_execute($statement);


    $response = array();
    if(!$statement){
        $response["success"] = false;
    }else{
        $response["success"] = true;
    }
    
 
   
    echo json_encode($response);
?>