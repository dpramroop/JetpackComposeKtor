<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\PDFController;
use App\Http\Controllers\WebCamController;
Route::get('generate-pdf', [PDFController::class, 'generatePDF']);
Route::get('/', function () {
    return view('welcome');
});

Route::post('/upload-selfie',[WebcamController::class,'index'])->name('upload-selfie');
Route::get('/qrcode',function(){
    return view('qrscanner');
});