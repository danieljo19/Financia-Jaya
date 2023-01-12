<?php
require('koneksi.php');

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST') {
    $type = $_POST["type"];
    $category = $_POST["category"];
    $note = $_POST["note"];
    $amount = $_POST["amount"];
    $date = $_POST["date"];
    $user_uid = $_POST["user_uid"];

    //$perintah = "INSERT INTO catatankeuangan(name, type, total, date, user_uid) VALUES('$name', '$type', '$total', '$date', '$user_uid')";
    
    if($type == "expenses") {
        $perintah = "INSERT INTO tb_expenses(type, category, note, amount, date, user_uid) VALUES('$type', '$category', '$note', '$amount', '$date', '$user_uid')";
    } else {
        $perintah = "INSERT INTO tb_incomes(type, category, note, amount, date, user_uid) VALUES('$type', '$category', '$note', '$amount', '$date', '$user_uid')";
    }
    
    $eksekusi = mysqli_query($konek, $perintah);
    $cek = mysqli_affected_rows($konek);
    
    if($cek > 0) {
        $response["kode"] = 1;
        $response["pesan"] = "Data has been saved successfully.";
    }else {
        $response["kode"] = 0;
        $response["pesan"] = "Failed to save the data.";
    }
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Tidak ada post data.";
}

echo json_encode($response);
mysqli_close($konek);