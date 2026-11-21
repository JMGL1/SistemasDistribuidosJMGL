<?php

use App\Http\Controllers\MultaController;
use Illuminate\Support\Facades\Route;

Route::post('/multa', [MultaController::class, 'registrar']);
Route::get('/multa/{codigo}', [MultaController::class, 'consultar']);
Route::get('/multas', [MultaController::class, 'listar']);
