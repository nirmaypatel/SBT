<?php
$username = $_POST['username'];
$password = $_POST['password'];

$request = file_get_contents("php://input")." ".$fname;

$servername = "localhost";
$dbuser = "anoopa";
$dbpassword = "anoopa";
$dbname = "smart_budget_tracker";


$response = array();
$response['request'] = $request;
$response['success'] = false;

// Create connection
$conn = new mysqli($servername, $dbuser, $dbpassword, $dbname);
// Check connection
if ($conn->connect_error) {
    $response['message'] = "Connection failed";
}else{
    $sql = "SELECT uid, fname, lname, age, email FROM user WHERE username = ?  AND password = ? LIMIT 1";
    $statement = $conn->prepare($sql);
    $statement->bind_param("ss", $username, $password);
    $statement->execute();
    
    $statement->bind_result($uid, $fname, $lname, $age, $email);
    if($statement->fetch()){
      $response['uid'] = $uid;
      $response['fname'] = $fname;
      $response['lname'] = $lname;
      $response['age'] = $age;
      $response['email'] = $email;
      $response['success'] = true;    
    }else{
      $response['message'] = 'No User found';
    }   
}   

$conn->close();

echo json_encode($response);

?>
