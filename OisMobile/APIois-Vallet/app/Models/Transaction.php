<?php

namespace App\models;

use Illuminate\Database\Eloquent\Model;

class Transaction extends Model
{
    protected $table = "tbl_trans";
    
    
    protected $fillable = ['REGNO','VEHCLASS','DATETIMEIN','DATETIMEOUT','USRNME','NOTRAN','NONOTA'];

    public $timestamps = false;
}
