<?php
require("koneksi.php");
$user_uid = $_POST["user_uid"];
$type = $_POST["type"];
$perintah = "SELECT tb_expenses.id, tb_expenses.type, tb_expenses_category.name, tb_expenses.note, tb_expenses.amount, tb_expenses.date, tb_expenses.user_uid FROM tb_expenses INNER JOIN tb_expenses_category ON tb_expenses.category = tb_expenses_category.id WHERE user_uid = '$user_uid' AND type = '$type' UNION ALL SELECT tb_incomes.id, tb_incomes.type, tb_incomes_category.name, tb_incomes.note, tb_incomes.amount, tb_incomes.date, tb_incomes.user_uid FROM tb_incomes INNER JOIN tb_incomes_category ON tb_incomes.category = tb_incomes_category.id WHERE user_uid = '$user_uid' AND type = '$type' ORDER BY date DESC";
$eksekusi = mysqli_query($konek, $perintah);
$cek = mysqli_affected_rows($konek);

if($cek > 0) {
    $response["kode"] = 1;
    $response["pesan"] = "Data Tersedia";
    $response["data"] = array();

    while($ambil = mysqli_fetch_object($eksekusi)) {
        $F["id"] = $ambil->id;
        $F["type"] = $ambil->type;
        $F["category"] = $ambil->name;
        $F["note"] = $ambil->note;
        $F["amount"] = $ambil->amount;
        $F["date"] = $ambil->date;
        $F["user_uid"] = $ambil->user_uid;
        
        array_push($response["data"], $F);
    }
} else {
    $response["kode"] = 0;
    $response["pesan"] = "Data Tidak Tersedia";
}

echo json_encode($response);
mysqli_close($konek);