<?php 
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $name = isset($_POST["name"]) ? $_POST["name"] : "";
    $startdate = isset($_POST["startdate"]) ? $_POST["startdate"] : "";

    $statement = mysqli_prepare($con, "DELETE FROM event_table WHERE id=? AND date=? AND name=?");
    mysqli_stmt_bind_param($statement, "sss", $id, $startdate, $name);
    mysqli_stmt_execute($statement);

    $startdate = strval(date('Y-m-d H:i:s', strtotime($startdate . '-3 hours')));

    $formatedate = str_replace(' ', '', $startdate);
    $formatedate = str_replace('-', '', $formatedate);
    $formatedate = str_replace(':', '', $formatedate);
    $event_name = $id."_".$name."_".$formatedate;

    $drop_query = "DROP EVENT IF EXISTS $event_name";
    

    $result = mysqli_query($con, $drop_query);

    $response = array();
    if(!$result){
        $response["success"] = false;
    }else{
        $response["success"] = true;
    }
    
    echo json_encode($response);
?>