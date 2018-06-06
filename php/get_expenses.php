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

$response['expenses'] = array();

if($token != 'token'){
  $response['message'] = "Unauthorized request";
}else{
  // Create connection
  $conn = new mysqli($servername, $dbuser, $dbpassword, $dbname);
  // Check connection
  if ($conn->connect_error) {
      $response['message'] = "Connection failed";
  }else{
      $sql = "SELECT e.ex_id, e.cat_id, c.cat_name, e.amt, e.t_stamp FROM expense e JOIN categories c on c.cat_id = e.cat_id WHERE c.uid = ? ORDER BY e.t_stamp DESC";
      $statement = $conn->prepare($sql);
      $statement->bind_param("d", $uid);
      $statement->execute();
      
      $statement->bind_result($ex_id, $cat_id, $cat_name, $amt, $t_stamp);
      while($statement->fetch()){
        $expense_rec = array();
        $expense_rec['ex_id'] = $ex_id;
        $expense_rec['cat_id'] = $cat_id;
        $expense_rec['cat_name'] = $cat_name;
        $expense_rec['amt'] = $amt;
        $expense_rec['t_stamp'] = $t_stamp;
        $response['expenses'][] = $expense_rec;
        $response['success'] = true;
      }
  }   

  $conn->close();  
}



echo json_encode($response);