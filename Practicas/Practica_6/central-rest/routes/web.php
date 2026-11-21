<?php

use Illuminate\Support\Facades\Route;

Route::get('/', function () {
    return redirect('/ciudadano.html');
});
Route::get('/consulta', function () {
    return redirect('/ciudadano.html');
});
