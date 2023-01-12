<?php
require('koneksi.php');

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id = $_POST["id"];
    $type = $_POST["type"];
    
    if($type == "expenses") {
        $perintah = "DELETE FROM tb_expenses WHERE id = '$id'";
    } else {
        $perintah = "DELETE FROM tb_incomes WHERE id = '$id'";
    }

    // $perintah = "DELETE FROM catatankeuangan WHERE id = '$id' AND uid = '$uid'";
    $eksekusi = mysqli_query($konek, $perintah);
    $cek = mysqli_affected_rows($konek);

    if($cek > 0) {
        $response["kode"] = 1;
        $response["pesan"] = "Data has been deleted.";
    }else {
        $response["kode"] = 0;
        $response["pesan"] = "Failed to delete the data";
    }
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Tidak ada Post Data";
}

echo json_encode($response);
mysqli_close($konek);