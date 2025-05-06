<?php

namespace App\Http\Controllers;
use Illuminate\Support\Facades\DB;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;

class UserController extends Controller
{
    //
    public function AllUsers()
    {
        $users= DB::table('users')->select('id','fname','lname','email')->get();
        return $users;
    }

    public function RemoveUser(Request $request)
    {
      Log::info($request);
       $deluser= User::find($request->id);
       $deluser->delete();

    }
    public function AddUser(Request $request)
    {
      Log::info($request);
       $adduser=User::create([
          'fname'=>$request->fname,
          'lname'=>$request->lname,
          'email'=>$request->email,
          'password'=>$request->password
       ]);

    }
    public function EditUser(Request $request)
    {
      Log::info($request);
       $finduser=User::findOrFail($request->id);
       Log::info($finduser);
      $finduser->fname= $request->fname;
      $finduser->lname= $request->lname;
      $finduser->email= $request->email;
      $finduser->save();

    }

}
