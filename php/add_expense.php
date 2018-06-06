<?php
$cat_id = $_POST['cat_id'];
$expense = $_POST['expense'];
$token = $_POST['token'];

$request = file_get_contents("php://input");

$response = array();
$response['request'] = $request;
$response['success'] = false;

$servername = "localhost";
$dbuser = "anoopa";
$dbpassword = "anoopa";
$dbname = "smart_budget_tracker";

if($token != 'token'){
    $response['message'] = "Unauthorized request";
}else{
    // Create connection
    $conn = new mysqli($servername, $dbuser, $dbpassword, $dbname);
    // Check connection
    if(empty($cat_id) || empty($expense)){
        $response['message'] = "incomplete data";
    }else if ($conn->connect_error) {
        $response['message'] = "connection failed";
    }else{ 
        $sql = "INSERT INTO expense (cat_id, amt)
    VALUES (?, ?)";
        $statement = $conn->prepare($sql);
        $statement->bind_param("dd", $cat_id, $expense);
        if ($statement->execute()) {
            $response['message'] = "Expense added successfully";
            $response['success'] = true;
            $response['ex_id'] = $statement->insert_id;
        } else {
            $response['message'] = "Error: " . $sql . "<br>" . $conn->error;
        }
    }

    $conn->close();  
}



echo json_encode($response);

?>