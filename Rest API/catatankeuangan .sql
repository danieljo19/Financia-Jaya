-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 21, 2022 at 12:52 PM
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
-- Table structure for table `catatankeuangan`
--

CREATE TABLE `catatankeuangan` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `type` enum('In','Out') NOT NULL,
  `total` int(11) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `catatankeuangan`
--

INSERT INTO `catatankeuangan` (`id`, `name`, `type`, `total`, `date`) VALUES
(1, 'Beli Bakso', 'Out', 12000, '2022-12-15'),
(2, 'Bonus', 'In', 2250000, '2022-12-01'),
(3, 'Kado', 'Out', 100000, '2022-12-18');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `catatankeuangan`
--
ALTER TABLE `catatankeuangan`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `catatankeuangan`
--
ALTER TABLE `catatankeuangan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
