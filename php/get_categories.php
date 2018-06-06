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

$response['categories'] = array();

if($token != 'token'){
  $response['message'] = "Unauthorized request";
}else{
  // Create connection
  $conn = new mysqli($servername, $dbuser, $dbpassword, $dbname);
  // Check connection
  if ($conn->connect_error) {
      $response['message'] = "Connection failed";
  }else{
      $sql = "SELECT cat_id, cat_name FROM categories WHERE uid = ?";
      $statement = $conn->prepare($sql);
      $statement->bind_param("d", $uid);
      $statement->execute();
      
      $statement->bind_result($cat_id, $cat_name);
      if($statement->errno == 0){
        $response['success'] = true;
      }
      while($statement->fetch()){
        $category_rec = array();
        $category_rec['cat_id'] = $cat_id;
        $category_rec['cat_name'] = $cat_name;
        $response['categories'][] = $category_rec;
      }
  }   

  $conn->close();  
}



echo json_encode($response);