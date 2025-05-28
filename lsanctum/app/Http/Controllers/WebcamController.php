<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class WebcamController extends Controller
{
    //
    public function index(Request $request){

        $base64image=$request->selfie;
        $tempFile=explode(',',$base64image);
        $image=count($tempFile)>1?$tempFile[1]:$tempFile[0];

        $decodedImage = base64_decode($image);
        $imageName = 'selfie' . time() . '.png';
        file_put_contents(public_path('/uploads/selfies//' . $imageName), $decodedImage);

        return back()->with('selfie',$imageName);

    }

    
}
