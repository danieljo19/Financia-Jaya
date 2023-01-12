-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jan 12, 2023 at 07:25 AM
-- Server version: 10.5.16-MariaDB
-- PHP Version: 7.3.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id20029733_catatankeuangan`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_expenses`
--

CREATE TABLE `tb_expenses` (
  `id` int(11) NOT NULL,
  `type` enum('expenses','incomes') COLLATE utf8_unicode_ci NOT NULL,
  `category` int(50) NOT NULL,
  `note` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `amount` int(11) NOT NULL,
  `date` date NOT NULL,
  `user_uid` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tb_expenses`
--

INSERT INTO `tb_expenses` (`id`, `type`, `category`, `note`, `amount`, `date`, `user_uid`) VALUES
(1, 'expenses', 1, 'Pizza', 75000, '2022-12-29', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(4, 'expenses', 3, 'GoCar MDP', 25000, '2022-12-30', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(5, 'expenses', 4, 'Lampu Rumah', 50000, '2022-12-30', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(15, 'expenses', 3, 'Retro', 20000, '2023-01-03', 'N9vYroWd8sSwZNbR69TgDcw6oh72'),
(28, 'expenses', 1, 'Mie Ayam', 13000, '2023-01-05', 'O8jD0s30BqUEyBxXGaTtkW1EYfT2'),
(49, 'expenses', 1, 'Nasi Padang', 10000, '2023-01-06', 'WVOImz25ERMCBsGaYRNxqBtxeVj1'),
(53, 'expenses', 1, 'Roti', 2500, '2023-01-07', 'WVOImz25ERMCBsGaYRNxqBtxeVj1'),
(54, 'expenses', 3, 'Angkot', 5000, '2023-01-07', 'WVOImz25ERMCBsGaYRNxqBtxeVj1'),
(55, 'expenses', 14, 'Badminton', 85000, '2023-01-07', 'WVOImz25ERMCBsGaYRNxqBtxeVj1'),
(57, 'expenses', 1, 'Hello', 500000, '2023-01-07', 'N9vYroWd8sSwZNbR69TgDcw6oh72'),
(59, 'expenses', 1, 'McDonald', 70000, '2023-01-08', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(60, 'expenses', 2, 'Nasi Goreng ', 10000, '2023-01-09', 'WVOImz25ERMCBsGaYRNxqBtxeVj1'),
(61, 'expenses', 5, 'BPJS Kesehatan', 35000, '2023-01-09', 'WVOImz25ERMCBsGaYRNxqBtxeVj1'),
(62, 'expenses', 5, 'Uang Sekolah Jesline', 400000, '2023-01-09', 'WVOImz25ERMCBsGaYRNxqBtxeVj1'),
(67, 'expenses', 10, 'Obat', 25000, '2023-01-09', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(68, 'expenses', 6, 'Bioskop', 45000, '2023-01-09', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(69, 'expenses', 1, 'Es Krim', 8000, '2023-01-09', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(70, 'expenses', 2, 'Kantong', 2500, '2023-01-09', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(73, 'expenses', 1, 'Roti', 58000, '2023-01-12', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(74, 'expenses', 13, 'Kado', 50000, '2023-01-12', 'WVOImz25ERMCBsGaYRNxqBtxeVj1'),
(75, 'expenses', 8, 'Gojek', 45000, '2023-01-12', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(76, 'expenses', 3, 'Gojek', 25000, '2023-01-11', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(77, 'expenses', 12, 'Sayur dan Buah', 230000, '2023-01-09', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(78, 'expenses', 1, 'Makan', 230000, '2022-12-15', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(79, 'expenses', 1, 'Nasi Padang', 17000, '2023-01-12', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2');

-- --------------------------------------------------------

--
-- Table structure for table `tb_expenses_category`
--

CREATE TABLE `tb_expenses_category` (
  `id` int(50) NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tb_expenses_category`
--

INSERT INTO `tb_expenses_category` (`id`, `name`) VALUES
(1, 'Food & Drink'),
(2, 'Shopping'),
(3, 'Transport'),
(4, 'Home'),
(5, 'Bills & Fees'),
(6, 'Entertainment'),
(7, 'Vehicle'),
(8, 'Travel'),
(9, 'Family & Personal'),
(10, 'Healthcare'),
(11, 'Education'),
(12, 'Groceries'),
(13, 'Gifts'),
(14, 'Sports & Hobby'),
(15, 'Beauty'),
(16, 'Work'),
(17, 'Pet'),
(18, 'Other');

-- --------------------------------------------------------

--
-- Table structure for table `tb_incomes`
--

CREATE TABLE `tb_incomes` (
  `id` int(11) NOT NULL,
  `type` enum('expenses','incomes') COLLATE utf8_unicode_ci NOT NULL,
  `category` int(50) NOT NULL,
  `note` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `amount` int(11) NOT NULL,
  `date` date NOT NULL,
  `user_uid` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tb_incomes`
--

INSERT INTO `tb_incomes` (`id`, `type`, `category`, `note`, `amount`, `date`, `user_uid`) VALUES
(1, 'incomes', 1, 'Gaji Kantor', 8500000, '2022-12-28', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(5, 'incomes', 1, 'Bonus', 2000000, '2023-01-02', 'WVOImz25ERMCBsGaYRNxqBtxeVj1'),
(7, 'incomes', 2, 'Bruh', 1000000, '2023-01-03', 'N9vYroWd8sSwZNbR69TgDcw6oh72'),
(8, 'incomes', 4, 'Tre', 2500000, '2023-01-04', 'N9vYroWd8sSwZNbR69TgDcw6oh72'),
(9, 'incomes', 2, '12212', 100000, '2023-01-04', 'N9vYroWd8sSwZNbR69TgDcw6oh72'),
(18, 'incomes', 8, 'Angpao', 200000, '2023-01-06', 'WVOImz25ERMCBsGaYRNxqBtxeVj1'),
(20, 'incomes', 4, 'Freelance', 5400000, '2023-01-08', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(21, 'incomes', 4, 'Bibit', 20000, '2023-01-09', 'WVOImz25ERMCBsGaYRNxqBtxeVj1'),
(22, 'incomes', 1, 'Gaji', 12000000, '2023-01-11', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(24, 'incomes', 1, 'Gaji', 12500000, '2022-11-30', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(25, 'incomes', 1, 'Gaji', 9850000, '2022-10-31', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(26, 'incomes', 1, 'Gaji', 8900000, '2022-09-30', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2');

-- --------------------------------------------------------

--
-- Table structure for table `tb_incomes_category`
--

CREATE TABLE `tb_incomes_category` (
  `id` int(50) NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tb_incomes_category`
--

INSERT INTO `tb_incomes_category` (`id`, `name`) VALUES
(1, 'Salary'),
(2, 'Business'),
(3, 'Gifts'),
(4, 'Extra Income'),
(5, 'Loan'),
(6, 'Investment'),
(7, 'Insurance Payout'),
(8, 'Other');

-- --------------------------------------------------------

--
-- Table structure for table `tb_users`
--

CREATE TABLE `tb_users` (
  `id` int(11) NOT NULL,
  `name` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(250) COLLATE utf8_unicode_ci NOT NULL,
  `user_uid` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tb_users`
--

INSERT INTO `tb_users` (`id`, `name`, `email`, `user_uid`) VALUES
(1, 'Daniel Johan', 'danieljohan37@gmail.com', 'WVOImz25ERMCBsGaYRNxqBtxeVj1'),
(8, 'Kucing Berlari', 'kucingberlari01@gmail.com', 'g0TnaBJfHKRlyAkuPYTBOA3yXOi2'),
(16, 'Danz', 'danieljohan0@gmail.com', 'O8jD0s30BqUEyBxXGaTtkW1EYfT2'),
(17, 'KevinKudou', 'kevinkudou12@gmail.com', 'dFMa3Y6MM9e8Xd2a7gCDTHO0bS72'),
(18, 'Niel', 'niel12@gmail.com', 'EQ5yLnx3RIQkDCbbyW2D8NDovwB3');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_expenses`
--
ALTER TABLE `tb_expenses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `category` (`category`);

--
-- Indexes for table `tb_expenses_category`
--
ALTER TABLE `tb_expenses_category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tb_incomes`
--
ALTER TABLE `tb_incomes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `category` (`category`);

--
-- Indexes for table `tb_incomes_category`
--
ALTER TABLE `tb_incomes_category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tb_users`
--
ALTER TABLE `tb_users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_expenses`
--
ALTER TABLE `tb_expenses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=81;

--
-- AUTO_INCREMENT for table `tb_incomes`
--
ALTER TABLE `tb_incomes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `tb_users`
--
ALTER TABLE `tb_users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tb_expenses`
--
ALTER TABLE `tb_expenses`
  ADD CONSTRAINT `tb_expenses_ibfk_1` FOREIGN KEY (`category`) REFERENCES `tb_expenses_category` (`id`);

--
-- Constraints for table `tb_incomes`
--
ALTER TABLE `tb_incomes`
  ADD CONSTRAINT `tb_incomes_ibfk_1` FOREIGN KEY (`category`) REFERENCES `tb_incomes_category` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
