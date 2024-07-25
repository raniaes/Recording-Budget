<?php 
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $name = isset($_POST["name"]) ? $_POST["name"] : "";
    $chose = isset($_POST["chose"]) ? $_POST["chose"] : "";
    $price = isset($_POST["price"]) ? $_POST["price"] : "";
    $startdate = isset($_POST["startdate"]) ? $_POST["startdate"] : "";
    $startdate = strval(date('Y-m-d H:i:s', strtotime($startdate . '+1 minute')));

    $statement = mysqli_prepare($con, "INSERT INTO event_table VALUES (?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "sssii", $id, $startdate, $name, $chose, $price);
    mysqli_stmt_execute($statement);

    $startdate = strval(date('Y-m-d H:i:s', strtotime($startdate . '-3 hours')));
    
    $formatedate = str_replace(' ', '', $startdate);
    $formatedate = str_replace('-', '', $formatedate);
    $formatedate = str_replace(':', '', $formatedate);
    $event_name = $id."_".$name."_".$formatedate;

    $query = "CREATE EVENT IF NOT EXISTS $event_name 
    ON SCHEDULE 
        EVERY 1 WEEK 
        STARTS '$startdate'
    ON COMPLETION NOT PRESERVE ENABLE DO 
    INSERT INTO money (id, date, name, chose, price, picture) 
    VALUES ('$id', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 3 HOUR), '$name', $chose, $price, null);";

    $result = mysqli_query($con, $query);



    $response = array();
    if(!$result){
        $response["success"] = false;
    }else{
        $response["success"] = true;
    }
    
    echo json_encode($response);
?>