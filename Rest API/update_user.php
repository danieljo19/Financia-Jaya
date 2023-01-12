<?php
require('koneksi.php');

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST') {
    $user_uid = $_POST["user_uid"];
    $name = $_POST["name"];

    $perintah = "UPDATE tb_users SET name = '$name' WHERE user_uid = '$user_uid'";
    $eksekusi = mysqli_query($konek, $perintah);
    $cek = mysqli_affected_rows($konek);
    $response["data"] = array();
    
    if($cek > 0) {
        $response["kode"] = 1;
        $response["pesan"] = "Your name has been updated.";
    }else {
        $response["kode"] = 0;
        $response["pesan"] = "Your name hasn't changed.";
    }
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Tidak ada Post Data";
}

echo json_encode($response);
mysqli_close($konek);