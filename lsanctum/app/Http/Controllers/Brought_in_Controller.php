<?php

namespace App\Http\Controllers;
use App\Models\Brought_in;
use Illuminate\Http\Request;

class Brought_in_Controller extends Controller
{
    //
public function AllBroughtIn()
    {
        $users= DB::table('brought_in')->select('id','fname','lname')->get();
        return $users;
    }

       public function AddBroughtIn(Request $request)
    {
      Log::info($request);
       $addbroughtin=Brought_In::create([
          'fname'=>$request->fname,
          'lname'=>$request->lname,
          'contact_no'=>$request->contact_no
          
       ]);

    }
}
