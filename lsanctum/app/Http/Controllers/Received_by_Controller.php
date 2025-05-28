<?php

namespace App\Http\Controllers;
use App\Models\Receive_by;
use Illuminate\Http\Request;

class Received_by_Controller extends Controller
{
    //

          public function AddReceived_By(Request $request)
    {
      Log::info($request);
       $addbroughtin=Receive_by::create([
          'fname'=>$request->fname,
          'lname'=>$request->lname,
          'contact_no'=>$request->contact_no
          
       ]);

    }
}
