<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\PDFController;

Route::get('generate-pdf', [PDFController::class, 'generatePDF']);
Route::get('/', function () {
    return view('welcome');
});
