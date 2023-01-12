<?php
require("koneksi.php");
$user_uid = $_POST["user_uid"];
$perintah = "SELECT tb_expenses.id, tb_expenses.type, tb_expenses_category.name, tb_expenses.note, tb_expenses.amount, tb_expenses.date, tb_expenses.user_uid FROM tb_expenses INNER JOIN tb_expenses_category ON tb_expenses.category = tb_expenses_category.id WHERE user_uid = '$user_uid' UNION ALL SELECT tb_incomes.id, tb_incomes.type, tb_incomes_category.name, tb_incomes.note, tb_incomes.amount, tb_incomes.date, tb_incomes.user_uid FROM tb_incomes INNER JOIN tb_incomes_category ON tb_incomes.category = tb_incomes_category.id WHERE user_uid = '$user_uid'";

$eksekusi = mysqli_query($konek, $perintah);
$cek = mysqli_affected_rows($konek);

if($cek > 0) {
    $totalIn = 0;
    $totalOut = 0;
    while($row = mysqli_fetch_assoc($eksekusi)) {
        if ($row['type'] == "incomes") {
            if($row['user_uid'] == $user_uid){
                $totalIn += $row['amount'];
            }
        } else {
            if($row['user_uid'] == $user_uid){
                $totalOut += $row['amount'];
            }
        }
    }
    $total = $totalIn - $totalOut;
    
    $response["total"] = $total;
    $response["kode"] = 1;
    $response["pesan"] = "Data is available.";
    $response["data"] = array();
    
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Data not found.";
}

echo json_encode($response);
mysqli_close($konek);