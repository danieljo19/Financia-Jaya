<?php
require("koneksi.php");
$user_uid = $_POST["user_uid"];
$date_start = date('Y-m-d', strtotime("-6 months"));
$date_end = date('Y-m-d');
$perintah = "SELECT MONTH(tb_incomes.date) as month, SUM(tb_incomes.amount) as total_incomes
FROM tb_incomes 
WHERE tb_incomes.user_uid = '$user_uid' AND tb_incomes.date BETWEEN '$date_start' AND '$date_end' 
GROUP BY MONTH(tb_incomes.date)
ORDER BY tb_incomes.date ASC";

$eksekusi = mysqli_query($konek, $perintah);
$cek = mysqli_affected_rows($konek);

if($cek > 0) {
    $response["kode"] = 1;
    $response["pesan"] = "Data is available.";
    $response["data"] = array();

    while($ambil = mysqli_fetch_object($eksekusi)) {
        $F["month"] = $ambil->month;
        $F["total_incomes"] = $ambil->total_incomes;
        
        array_push($response["data"], $F);
    }
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Data not found.";
}

echo json_encode($response);
mysqli_close($konek);