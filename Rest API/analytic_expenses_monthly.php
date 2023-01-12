<?php
require("koneksi.php");
$user_uid = $_POST["user_uid"];
$date_start = date('Y-m-d', strtotime("-6 months"));
$date_end = date('Y-m-d');
$perintah = "SELECT tb_expenses_category.name as category, SUM(amount) as total FROM tb_expenses 
INNER JOIN tb_expenses_category ON tb_expenses.category = tb_expenses_category.id 
WHERE user_uid = '$user_uid' and date between '$date_start' and '$date_end'
GROUP BY tb_expenses.category";

$eksekusi = mysqli_query($konek, $perintah);
$cek = mysqli_affected_rows($konek);

if($cek > 0) {
    $response["kode"] = 1;
    $response["pesan"] = "Data is available.";
    $response["data"] = array();

    while($ambil = mysqli_fetch_object($eksekusi)) {
        $F["category"] = $ambil->category;
        $F["total"] = $ambil->total;
        
        array_push($response["data"], $F);
    }
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Data not found.";
}

echo json_encode($response);
mysqli_close($konek);