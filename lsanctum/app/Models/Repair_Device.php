<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Repair_Device extends Model
{
    //
    protected $table = 'repair_device';
    protected $fillable = [
        'deviceid',
        'serial_no',
        'issue',
        'location',
    ];
}
