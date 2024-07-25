<?php 
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $chose = isset($_POST["chose"]) ? $_POST["chose"] : "";
    $startdate = isset($_POST["startdate"]) ? $_POST["startdate"] : "";
    $re_startdate = isset($_POST["re_startdate"]) ? $_POST["re_startdate"] : "";
    $name = isset($_POST["name"]) ? $_POST["name"] : "";
    $re_name = isset($_POST["re_name"]) ? $_POST["re_name"] : "";
    $price = isset($_POST["price"]) ? $_POST["price"] : "";

    $re_startdate = strval(date('Y-m-d H:i:s', strtotime($re_startdate . '+1 minute')));

    $statement = mysqli_prepare($con, "UPDATE event_table SET name=?, chose=?, price=?, date=? WHERE id=? AND date=? AND name=?");
    mysqli_stmt_bind_param($statement, "siissss", $re_name, $chose, $price, $re_startdate, $id, $startdate, $name);
    mysqli_stmt_execute($statement);

    $startdate = strval(date('Y-m-d H:i:s', strtotime($startdate . '-3 hours')));

    $formatedate = str_replace(' ', '', $startdate);
    $formatedate = str_replace('-', '', $formatedate);
    $formatedate = str_replace(':', '', $formatedate);
    $event_name = $id."_".$name."_".$formatedate;

    $drop_query = "DROP EVENT IF EXISTS $event_name";
    mysqli_query($con, $drop_query);

    $re_startdate = strval(date('Y-m-d H:i:s', strtotime($re_startdate . '-3 hours')));

    $formatedate = str_replace(' ', '', $re_startdate);
    $formatedate = str_replace('-', '', $formatedate);
    $formatedate = str_replace(':', '', $formatedate);
    $event_name = $id."_".$re_name."_".$formatedate;

    $query = "CREATE EVENT IF NOT EXISTS $event_name 
    ON SCHEDULE 
        EVERY 1 WEEK 
        STARTS '$re_startdate'
    ON COMPLETION NOT PRESERVE ENABLE DO 
    INSERT INTO money (id, date, name, chose, price, picture) 
    VALUES ('$id', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 3 HOUR), '$re_name', $chose, $price, null);";

    $result = mysqli_query($con, $query);

    $response = array();
    if(!$result){
        $response["success"] = false;
    }else{
        $response["success"] = true;
    }
    
    echo json_encode($response);
?>