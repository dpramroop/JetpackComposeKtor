<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Receive_by extends Model
{
    //
      protected $table = 'receive_by';
    protected $fillable = [
        'fname',
        'lname',
        'contact_no'
        
    ];

}
