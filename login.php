<?php

if (!empty($_POST['username'] && !empty($_POST['password'])) {
    $email = $_POST['username'];
    $password = $_POST['password'];

    $con = mysqli_connect("localhost", "root", "", "athena_db")
}
