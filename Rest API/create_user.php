<?php
require('koneksi.php');

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST') {
    $name = $_POST["name"];
    $email = $_POST["email"];
    $user_uid = $_POST["user_uid"];

    $perintah = "INSERT INTO tb_users(name, email, user_uid) VALUES('$name', '$email', '$user_uid')";
    
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