<?php

namespace App\models;

use Illuminate\Database\Eloquent\Model;

class Shift extends Model
{
    protected $table = "tbl_shift";

    public $timestamps = false;

    protected $fillable = ['INITNM','STATUS','CODE','SDTTMIN','SDTTMOUT','GATE','POS'];
}
