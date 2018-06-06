<?php
$username = trim($_POST['username']);
$fname = trim($_POST['fname']);
$lname = trim($_POST['lname']);
$age = trim($_POST['age']);
$email = trim($_POST['email']);
$password = trim($_POST['password']);

$request = file_get_contents("php://input")." ".$fname;

$response = array();
$response['request'] = $request;
$response['success'] = false;

$servername = "localhost";
$dbuser = "anoopa";
$dbpassword = "anoopa";
$dbname = "smart_budget_tracker";

// Create connection
$conn = new mysqli($servername, $dbuser, $dbpassword, $dbname);
// Check connection
if(empty($username) || empty($fname) || empty($lname) || empty($email) || empty($age) || empty($password)){
    $response['message'] = "incomplete data";
}else if ($conn->connect_error) {
    $response['message'] = "connection failed";
}else{ 
    $sql = "INSERT INTO user (username, fname, lname, age, email, password)
VALUES (?, ?, ?, ?, ?, ?)";
    $statement = $conn->prepare($sql);
    $statement->bind_param("sssdss", $username, $fname, $lname, $age, $email, $password);
    if ($statement->execute()) {
        $response['message'] = "New record created successfully";
        $response['success'] = true;
        $response['uid'] = $statement->insert_id;
    } else {
        $response['message'] = "Error: " . $sql . "<br>" . $conn->error;
    }
}

$conn->close();

echo json_encode($response);

?>
