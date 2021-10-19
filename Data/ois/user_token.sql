-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 19, 2021 at 06:06 AM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 8.0.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ois`
--

-- --------------------------------------------------------

--
-- Table structure for table `user_token`
--

CREATE TABLE `user_token` (
  `id` int(11) NOT NULL,
  `username` varchar(25) NOT NULL,
  `token` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_token`
--

INSERT INTO `user_token` (`id`, `username`, `token`, `created_at`) VALUES
(1, 'AUD', 'EEsodHvccZKWRG0dP8N9xBp1s9pqCat8lnT4xIQVwdUddKW3gfEdCxji4Ep7eeFln554RdxHkvgATHco', '2021-10-14 15:11:17'),
(2, 'AUD', 'n3mKhwdjm5TCZdv2bo5GhnkWNh6j8SKWqjcKLjsaBsDdvk0oH0CVTQTt47mdxzQMzgNIXsS0CVv5e98A', '2021-10-14 15:11:42'),
(3, 'AUD', '5SghFFdJ0O7GbxquUdIJZ0cTDqZPkEtiFwF6knBCtB5GFUs26dwLpqsuRDZOnTV4gVbq8dhUOxQdeKDA', '2021-10-14 15:18:24'),
(4, 'AUD', 'lmU1APkW2dSdbJcd4aXCdJig5uVP7ENkczxeIOCLULYzNnwsYr7YtsuaL1ZkW2ZRlk1kYJdBLRKL9YpZ', '2021-10-19 00:44:09'),
(5, 'AUD', '8Y3BvsAaSYdWKEfaVyOtmLUxn5J667sHCFfNdFOUmncQldGqb370U7SscyAtgUQ9sjd2K2GKLLpvFuVs', '2021-10-19 00:45:15'),
(6, 'AUD', '7bfVRHSHr7kIFnGRreJxjwcMbxst1RY3CIdy1u4CdXXsXOz2h9jONQtXPj6svAYlAfkDdUdxt19xiHmC', '2021-10-19 02:47:41');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `user_token`
--
ALTER TABLE `user_token`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `user_token`
--
ALTER TABLE `user_token`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
