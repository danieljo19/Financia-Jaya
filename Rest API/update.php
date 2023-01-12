<?php
require('koneksi.php');

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id = $_POST["id"];
    $type = $_POST["type"];
    $category = $_POST["category"];
    $note = $_POST["note"];
    $amount = $_POST["amount"];
    $date = $_POST["date"];
    
    if($type == "expenses") {
        $perintah = "UPDATE tb_expenses SET category = '$category', note = '$note', amount = '$amount', date = '$date' WHERE id = '$id' AND type = '$type'";
    } else {
        $perintah = "UPDATE tb_incomes SET category = '$category', note = '$note', amount = '$amount', date = '$date' WHERE id = '$id' AND type = '$type'";
    }

    // $perintah = "UPDATE catatankeuangan SET name = '$name', type = '$type', total = '$total', date = '$date', uid = '$uid' WHERE id = '$id' AND uid = '$uid'";
    $eksekusi = mysqli_query($konek, $perintah);
    $cek = mysqli_affected_rows($konek);
    $response["data"] = array();
    
    if($cek > 0) {
        $response["kode"] = 1;
        $response["pesan"] = "Data has been updated.";
    }else {
        $response["kode"] = 0;
        $response["pesan"] = "Data has been deleted.";
    }
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Tidak ada Post Data";
}

echo json_encode($response);
mysqli_close($konek);