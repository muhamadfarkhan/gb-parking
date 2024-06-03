<?php

namespace App\models;

use Illuminate\Database\Eloquent\Model;

class Parameter extends Model
{
    protected $table = "mst_par";

    public $timestamps = false;

    protected $fillable = ['FLDNM','CODE','IMEI','LUPDDTTIME'];

   
}
