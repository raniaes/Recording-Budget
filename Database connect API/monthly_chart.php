<?php
    $con = mysqli_connect("localhost", "sgm1991", "ahrrms12!", "sgm1991");
    mysqli_query($con,'SET NAMES utf8');

    $id = isset($_POST["id"]) ? $_POST["id"] : "";
    $year = isset($_POST["year"]) ? $_POST["year"] : "";

    $statement = mysqli_prepare($con, "SELECT price, chose, date FROM money WHERE id = ? AND YEAR(date) = ?");
    mysqli_stmt_bind_param($statement, "ss", $id, $year);
    mysqli_stmt_execute($statement);

    $result = mysqli_stmt_get_result($statement);

    $response = array();
    $response["success"] = false;
    $response["jan"] = 0;
    $response["feb"] = 0;
    $response["mar"] = 0;
    $response["apr"] = 0;
    $response["may"] = 0;
    $response["jun"] = 0;
    $response["jul"] = 0;
    $response["aug"] = 0;
    $response["sept"] = 0;
    $response["oct"] = 0;
    $response["nov"] = 0;
    $response["dec"] = 0;

    while($row = mysqli_fetch_assoc($result)) {
        $response["success"] = true;
        $date = $row['date'];
        $month = date('m', strtotime($date));
        
        if ($month == '01') {
            if ($row['chose'] == true) {
                $response["jan"] += $row['price'];
            } else {
                $response["jan"] -= $row['price'];
            }  
        }elseif ($month == '02'){
            if ($row['chose'] == true) {
                $response["feb"] += $row['price'];
            } else {
                $response["feb"] -= $row['price'];
            }  
        }elseif ($month == '03'){
            if ($row['chose'] == true) {
                $response["mar"] += $row['price'];
            } else {
                $response["mar"] -= $row['price'];
            }  
        }elseif ($month == '04'){
            if ($row['chose'] == true) {
                $response["apr"] += $row['price'];
            } else {
                $response["apr"] -= $row['price'];
            }  
        }elseif ($month == '05'){
            if ($row['chose'] == true) {
                $response["may"] += $row['price'];
            } else {
                $response["may"] -= $row['price'];
            }  
        }elseif ($month == '06'){
            if ($row['chose'] == true) {
                $response["jun"] += $row['price'];
            } else {
                $response["jun"] -= $row['price'];
            }  
        }elseif ($month == '07'){
            if ($row['chose'] == true) {
                $response["jul"] += $row['price'];
            } else {
                $response["jul"] -= $row['price'];
            }  
        }elseif ($month == '08'){
            if ($row['chose'] == true) {
                $response["aug"] += $row['price'];
            } else {
                $response["aug"] -= $row['price'];
            }  
        }elseif ($month == '09'){
            if ($row['chose'] == true) {
                $response["sept"] += $row['price'];
            } else {
                $response["sept"] -= $row['price'];
            }  
        }elseif ($month == '10'){
            if ($row['chose'] == true) {
                $response["oct"] += $row['price'];
            } else {
                $response["oct"] -= $row['price'];
            }  
        }elseif ($month == '11'){
            if ($row['chose'] == true) {
                $response["nov"] += $row['price'];
            } else {
                $response["nov"] -= $row['price'];
            }  
        }else{
            if ($row['chose'] == true) {
                $response["dec"] += $row['price'];
            } else {
                $response["dec"] -= $row['price'];
            }  
        }     
    }    
    
    echo json_encode($response); // 더한 값 출력

?>