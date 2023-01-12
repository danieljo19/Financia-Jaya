<?php
require('koneksi.php');

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id = $_POST["id"];
    $type = $_POST["type"];
    
    if($type == "expenses") {
        $perintah = "SELECT * FROM tb_expenses WHERE id = '$id'";
    } else {
        $perintah = "SELECT * FROM tb_incomes WHERE id = '$id'";
    }

    // $perintah = "SELECT * FROM catatankeuangan WHERE Uid = '$uid' AND id = '$id'";
    $eksekusi = mysqli_query($konek, $perintah);
    $cek = mysqli_affected_rows($konek);
    $response["data"] = array();
    
    if($cek > 0) {
        $response["kode"] = 1;
        $response["pesan"] = "Data Tersedia";

        while($ambil = mysqli_fetch_object($eksekusi)) {
            $F["id"] = $ambil->id;
            $F["type"] = $ambil->type;
            $F["category"] = $ambil->category;
            $F["note"] = $ambil->note;
            $F["amount"] = $ambil->amount;
            $F["date"] = $ambil->date;
            $F["user_uid"] = $ambil->user_uid;
            
            array_push($response["data"], $F);
        }
    }else {
        $response["kode"] = 0;
        $response["pesan"] = "Data tidak Tersedia";
    }
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Tidak ada Post Data";
}

echo json_encode($response);
mysqli_close($konek);