<?php
$uid = $_POST['uid'];
$token = $_POST['token'];

$request = file_get_contents("php://input");

$servername = "localhost";
$dbuser = "anoopa";
$dbpassword = "anoopa";
$dbname = "smart_budget_tracker";


$response = array();
$response['request'] = $request;
$response['success'] = false;

$response['goals'] = array();

if($token != 'token'){
  $response['message'] = "Unauthorized request";
}else{
  // Create connection
  $conn = new mysqli($servername, $dbuser, $dbpassword, $dbname);
  // Check connection
  if ($conn->connect_error) {
      $response['message'] = "Connection failed";
  }else{
      $sql = "SELECT cat_id, cat_name, goal FROM categories WHERE uid = ? AND goal IS NOT NULL";
      $statement = $conn->prepare($sql);
      $statement->bind_param("d", $uid);
      $statement->execute();
      
      $statement->bind_result($cat_id, $cat_name, $goal);
      while($statement->fetch()){
        $goal_rec = array();
        $goal_rec['cat_id'] = $cat_id;
        $goal_rec['cat_name'] = $cat_name;
        $goal_rec['goal'] = $goal;
        $response['goals'][] = $goal_rec;
        $response['success'] = true;
      }
  }   

  $conn->close();  
}



echo json_encode($response);