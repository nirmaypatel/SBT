<?php
$uid = $_POST['uid'];
$cat_name = $_POST['cat_name'];
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
    if(empty($uid) || empty($cat_name)){
        $response['message'] = "incomplete data";
    }else if ($conn->connect_error) {
        $response['message'] = "connection failed";
    }else{ 
        $sql = "INSERT INTO categories (uid, cat_name)
    VALUES (?, ?)";
        $statement = $conn->prepare($sql);
        $statement->bind_param("ds", $uid, $cat_name);
        if ($statement->execute()) {
            $response['message'] = "Category added successfully";
            $response['success'] = true;
            $response['cat_id'] = $statement->insert_id;
        } else {
            $response['message'] = "Error: " . $sql . "<br>" . $conn->error;
        }
    }

    $conn->close();  
}



echo json_encode($response);

?>