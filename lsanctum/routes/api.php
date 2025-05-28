<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UserController;
use App\Http\Controllers\Brought_in_Controller;
use App\Models\User;
use Illuminate\Support\Facades\Log;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');


Route::get('/users',  [UserController::class, 'AllUsers']);
Route::post('/users',  [UserController::class, 'RemoveUser']);
Route::post('/adduser',  [UserController::class, 'AddUser']);
Route::put('/edituser',  [UserController::class, 'EditUser']);

Route::post('/loginuser', function (Request $request) {
    Log::info($request);
    $request->validate([
        'email' => 'required|email',
        'password' => 'required',
        'device_name' => 'required',
    ]);
 
    $user = User::where('email', $request->email)->first();
 
    if (! $user || ! Hash::check($request->password, $user->password)) {
        throw ValidationException::withMessages([
            'email' => ['The provided credentials are incorrect.'],
        ]);
    }
   
 
    return $user->createToken($request->device_name)->plainTextToken;
});


Route::post('/testtoken', function (Request $request) {
    Log::info($request);
    return $request;
})->middleware('auth:sanctum');
// ->middleware('auth:sanctum')

Route::post('/AddBroughtIn',[Brought_in_Controller::class,'AddBroughtIn']);