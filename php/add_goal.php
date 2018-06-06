<?php
$cat_id = $_POST['cat_id'];
$goal = $_POST['goal'];
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
    if(empty($cat_id) || empty($goal)){
        $response['message'] = "incomplete data";
    }else if ($conn->connect_error) {
        $response['message'] = "connection failed";
    }else{ 
        $sql = "UPDATE categories SET goal = ? WHERE cat_id = ?";
        $statement = $conn->prepare($sql);
        $statement->bind_param("dd", $goal, $cat_id);
        if ($statement->execute()) {
            $response['message'] = "Goal added successfully";
            $response['success'] = true;
        } else {
            $response['message'] = "Error: " . $sql . "<br>" . $conn->error;
        }
    }

    $conn->close();  
}



echo json_encode($response);

?>