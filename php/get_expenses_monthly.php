<?php
$uid = $_POST['uid'];
$month = $_POST['month']? $_POST['month']:date('m');
$year = $_POST['year']? $_POST['year']:date('Y');
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
      $sql = "SELECT e.cat_id, c.cat_name, SUM(e.amt) as total_amt FROM expense e JOIN categories c on c.cat_id = e.cat_id WHERE c.uid = ? AND DATE_FORMAT(t_stamp, '%Y') = ? AND DATE_FORMAT(t_stamp, '%m') = ? GROUP BY c.cat_id ORDER BY c.cat_id";
      $statement = $conn->prepare($sql);
      $statement->bind_param("ddd", $uid, $year, $month);
      $statement->execute();
      
      $statement->bind_result($cat_id, $cat_name, $total_amt);
      while($statement->fetch()){
        $expense_rec = array();
        $expense_rec['cat_id'] = $cat_id;
        $expense_rec['cat_name'] = $cat_name;
        $expense_rec['total_amt'] = $total_amt;
        $response['expenses'][] = $expense_rec;
        $response['success'] = true;
      }
  }   

  $conn->close();  
}



echo json_encode($response);