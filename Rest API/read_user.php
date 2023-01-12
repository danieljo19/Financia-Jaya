<?php
require('koneksi.php');

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST') {
    $user_uid = $_POST["user_uid"];
    
    $perintah = "SELECT * FROM tb_users WHERE user_uid = '$user_uid'";
    $eksekusi = mysqli_query($konek, $perintah);
    $cek = mysqli_affected_rows($konek);
    $response["data"] = array();
    
    if($cek > 0) {
        $response["kode"] = 1;
        $response["pesan"] = "Data Tersedia";

        while($ambil = mysqli_fetch_object($eksekusi)) {
            $F["id"] = $ambil->id;
            $F["name"] = $ambil->name;
            $F["email"] = $ambil->email;
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