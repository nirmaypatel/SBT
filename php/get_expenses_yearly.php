<?php
$uid = $_POST['uid'];
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
$expenses = array();

for($i =1; $i <= 12; $i++){
  $expenses[$i] = 0;
}

if($token != 'token'){
  $response['message'] = "Unauthorized request";
}else{
  // Create connection
  $conn = new mysqli($servername, $dbuser, $dbpassword, $dbname);
  // Check connection
  if ($conn->connect_error) {
      $response['message'] = "Connection failed";
  }else{
      $sql = "SELECT DATE_FORMAT(t_stamp, '%c') as month, SUM(e.amt) as total_amt FROM expense e JOIN categories c on c.cat_id = e.cat_id WHERE c.uid = ? AND DATE_FORMAT(t_stamp, '%Y') = ? GROUP BY DATE_FORMAT(t_stamp, '%c')";
      $statement = $conn->prepare($sql);
      $statement->bind_param("dd", $uid, $year);
      $statement->execute();
      
      $statement->bind_result($month, $total_amt);
      while($statement->fetch()){
        $expenses[$month] = $total_amt;
        $response['success'] = true;
      }

      for($i = 1; $i <= 12; $i++){
        $expense_rec = array();
        $expense_rec['month'] = $i;
        $expense_rec['total_amt'] = $expenses[$i];
        $response['expenses'][] = $expense_rec;

      }
  }   

  $conn->close();  
}



echo json_encode($response);